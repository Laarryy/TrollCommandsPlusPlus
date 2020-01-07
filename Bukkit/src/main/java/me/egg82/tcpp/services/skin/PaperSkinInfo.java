package me.egg82.ae.services.skin;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import me.egg82.ae.services.lookup.PlayerLookup;
import me.egg82.ae.services.material.MaterialLookup;
import me.egg82.ae.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.mineskin.MineskinClient;
import org.mineskin.data.Skin;
import org.mineskin.data.SkinCallback;
import org.mineskin.data.SkinData;
import org.mineskin.data.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaperSkinInfo implements SkinInfo {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private UUID uuid;
    private String name;
    private Skin skin;

    private static Material skullMaterial;

    static {
        Optional<Material> m = MaterialLookup.get("PLAYER_HEAD", "SKULL_ITEM");
        if (!m.isPresent()) {
            throw new RuntimeException("Could not get skull material.");
        }
        skullMaterial = m.get();
    }

    private Gson gson = new Gson();
    private MineskinClient client = new MineskinClient();

    private static Cache<UUID, Skin> skinCache = Caffeine.newBuilder().expireAfterWrite(1L, TimeUnit.DAYS).build();

    private static final Object skinCacheLock = new Object();

    public PaperSkinInfo(UUID uuid, File cacheDir) throws IOException {
        this.uuid = uuid;
        this.name = PlayerLookup.get(uuid).getName();

        Optional<Skin> skin = Optional.ofNullable(skinCache.getIfPresent(uuid));
        if (!skin.isPresent()) {
            synchronized (skinCacheLock) {
                skin = Optional.ofNullable(skinCache.getIfPresent(uuid));
                if (!skin.isPresent()) {
                    skin = Optional.of(skinExpensive(uuid, cacheDir));
                    skinCache.put(uuid, skin.get());
                }
            }
        }

        this.skin = skin.get();
    }

    public PaperSkinInfo(UUID uuid, String name, Skin skin) {
        this.uuid = uuid;
        this.name = name;
        this.skin = skin;
    }

    public PaperSkinInfo(UUID uuid, String name, String texture, String textureUrl, String textureSignature) {
        this.uuid = uuid;
        this.name = name;
        this.skin = createSkin(uuid, name, texture, textureUrl, textureSignature);
    }

    public UUID getUUID() { return uuid; }

    public Skin getSkin() { return skin; }

    public ItemStack getSkull() { return getSkull(1); }

    public ItemStack getSkull(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("amount cannot be < 1.");
        }

        ItemStack retVal = new ItemStack(skullMaterial, amount);
        retVal.setDurability((short) 3);

        ItemMeta meta = retVal.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(skullMaterial);
        }
        meta.setDisplayName(name);

        SkullMeta skull = (SkullMeta) meta;
        skull.setPlayerProfile(getProfile());

        retVal.setItemMeta(meta);

        return retVal;
    }

    private Skin createSkin(UUID uuid, String name, String texture, String textureUrl, String textureSignature) {
        Skin retVal = new Skin();
        retVal.id = -1;
        retVal.name = name;
        retVal.data = createData(uuid, texture, textureUrl, textureSignature);
        retVal.timestamp = System.currentTimeMillis();
        retVal.prvate = false;
        retVal.views = 0;
        retVal.accountId = -1;
        retVal.nextRequest = 0.0d;
        return retVal;
    }

    private SkinData createData(UUID uuid, String texture, String textureUrl, String textureSignature) {
        SkinData retVal = new SkinData();
        retVal.uuid = uuid;
        retVal.texture = new Texture();
        retVal.texture.value = texture;
        retVal.texture.signature = textureSignature;
        retVal.texture.url = textureUrl;
        return retVal;
    }

    private Skin skinExpensive(UUID playerUuid, File cacheDir) throws IOException {
        Skin retVal;

        File skinFile = getSkinFile(playerUuid, cacheDir);

        // Lookup from file cache
        if (skinFile.exists()) {
            try (FileReader reader = new FileReader(skinFile)) {
                retVal = gson.fromJson(reader, Skin.class);
            }

            if (retVal != null) {
                if (!expired(retVal)) {
                    return retVal;
                }
            }
        }

        // Grab skin from network/Mojang
        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<Skin> atomicRetVal = new AtomicReference<>(null);
        AtomicBoolean isDefault = new AtomicBoolean(false);

        client.generateUser(playerUuid, new SkinCallback() {
            public void done(Skin skin) {
                atomicRetVal.set(skin);
                latch.countDown();
            }

            public void error(String message) {
                logger.error(message);
                latch.countDown();
            }

            public void exception(Exception ex) {
                // Interpret a timeout as "no/default skin"
                if (ex instanceof SocketTimeoutException) {
                    isDefault.set(true);
                    latch.countDown();
                    return;
                }

                logger.error(ex.getMessage(), ex);
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException(ex.getMessage(), ex);
        }

        if (isDefault.get()) {
            // Player is using the default skin
            writeToFileCache(atomicRetVal.get(), playerUuid, cacheDir);
            return atomicRetVal.get();
        }

        if (atomicRetVal.get() == null) {
            throw new IOException("Could not get player skin.");
        }

        return atomicRetVal.get();
    }

    private PlayerProfile getProfile() {
        PlayerProfile retVal = Bukkit.createProfile(uuid, name);
        retVal.setProperty(new ProfileProperty("textures", skin.data.texture.value));
        return retVal;
    }

    private boolean expired(Skin skin) {
        Optional<Long> time = TimeUtil.getTime("1day");
        if (!time.isPresent()) {
            return false;
        }

        long offset = skin.timestamp * 1000L - System.currentTimeMillis();
        return offset < time.get();
    }

    private void writeToFileCache(Skin skin, UUID playerUuid, File cacheDir) throws IOException {
        File skinFile = getSkinFile(playerUuid, cacheDir);

        try (FileWriter writer = new FileWriter(skinFile, false)) {
            gson.toJson(skin, writer);
            writer.flush();
        }
    }

    private File getSkinFile(UUID playerUuid, File cacheDir) throws IOException {
        if (cacheDir.exists() && !cacheDir.isDirectory()) {
            Files.delete(cacheDir.toPath());
        }
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                throw new IOException("Could not create parent directory structure.");
            }
        }

        File retVal = new File(cacheDir, playerUuid.toString() + ".json");
        if (retVal.exists() && retVal.isDirectory()) {
            Files.delete(retVal.toPath());
        }

        return retVal;
    }
}
