package me.egg82.ae.hooks;

import com.djrapitops.plan.capability.CapabilityService;
import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.ExtensionService;
import com.djrapitops.plan.extension.FormatType;
import com.djrapitops.plan.extension.annotation.NumberProvider;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.annotation.TableProvider;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import com.djrapitops.plan.extension.icon.Icon;
import com.djrapitops.plan.extension.table.Table;
import java.util.*;
import me.egg82.ae.EnchantAPI;
import me.egg82.ae.api.*;
import me.egg82.ae.services.entity.EntityItemHandler;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerAnalyticsHook implements PluginHook {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CapabilityService capabilities;

    public PlayerAnalyticsHook() {
        capabilities = CapabilityService.getInstance();

        if (isCapabilityAvailable("DATA_EXTENSION_VALUES") && isCapabilityAvailable("DATA_EXTENSION_TABLES")) {
            try {
                ExtensionService.getInstance().register(new Data());
            } catch (NoClassDefFoundError ex) {
                // Plan not installed
                logger.error("Plan is not installed.", ex);
            } catch (IllegalStateException ex) {
                // Plan not enabled
                logger.error("Plan is not enabled.", ex);
            } catch (IllegalArgumentException ex) {
                // DataExtension impl error
                logger.error("DataExtension implementation exception.", ex);
            }
        }
    }

    public void cancel() { }

    private boolean isCapabilityAvailable(String capability) {
        try {
            return capabilities.hasCapability(capability);
        } catch (NoClassDefFoundError ignored) {
            return false;
        }
    }

    @PluginInfo(
            name = "AdvancedEnchantments",
            iconName = "book",
            iconFamily = Family.SOLID,
            color = Color.PURPLE
    )
    class Data implements DataExtension {
        private final EnchantAPI api = EnchantAPI.getInstance();
        private final CallEvents[] events = new CallEvents[] { CallEvents.SERVER_PERIODICAL, CallEvents.PLAYER_JOIN, CallEvents.PLAYER_LEAVE, CallEvents.SERVER_EXTENSION_REGISTER };
        private EntityItemHandler entityItemHandler;
        private final BukkitEnchantment durability = BukkitEnchantment.fromEnchant(Enchantment.DURABILITY);
        private final int numEnchants = AdvancedEnchantment.values().size();

        private Data() {
            try {
                entityItemHandler = ServiceLocator.get(EntityItemHandler.class);
            } catch (InstantiationException | IllegalAccessException | ServiceNotFoundException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        @NumberProvider(
                text = "Registered Enchants",
                description = "Number of registered custom enchants.",
                priority = 1,
                iconName = "book",
                iconFamily = Family.SOLID,
                iconColor = Color.NONE,
                format = FormatType.NONE
        )
        public long getEnchants() { return numEnchants; }

        @NumberProvider(
                text = "Souls",
                description = "Number of souls the player possesses.",
                priority = 1,
                iconName = "ghost",
                iconFamily = Family.SOLID,
                iconColor = Color.NONE,
                format = FormatType.NONE
        )
        public long getSouls(UUID playerUUID) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                return 0;
            }

            int souls = 0;
            Set<GenericEnchantableItem> items = getItems(player);
            for (GenericEnchantableItem item : items) {
                souls += item.getSouls();
            }
            return souls;
        }

        @TableProvider()
        public Table getSoulsPerItem(UUID playerUUID) {
            Table.Factory retVal = Table.builder()
                    .columnOne("Item", Icon.called("user-shield").of(Family.SOLID).build())
                    .columnTwo("Souls", Icon.called("ghost").of(Family.SOLID).build());

            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                return retVal.build();
            }

            Set<GenericEnchantableItem> items = getItems(player);
            for (GenericEnchantableItem item : items) {
                if (item.getSouls() > 0) {
                    retVal.addRow(((ItemStack) item.getConcrete()).getType(), item.getSouls());
                }
            }
            return retVal.build();
        }

        @NumberProvider(
                text = "Enchants",
                description = "Number of enchants the player possesses.",
                iconName = "file",
                iconFamily = Family.SOLID,
                iconColor = Color.NONE,
                format = FormatType.NONE
        )
        public long getEnchants(UUID playerUUID) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                return 0;
            }

            int enchants = 0;
            Set<GenericEnchantableItem> items = getItems(player);
            for (GenericEnchantableItem item : items) {
                for (Map.Entry<GenericEnchantment, Integer> kvp : item.getEnchantments().entrySet()) {
                    if (kvp.getKey().equals(durability) && kvp.getValue() <= 0) {
                        continue;
                    }
                   enchants += 1;
                }
            }
            return enchants;
        }

        @TableProvider()
        public Table getEnchantsPerItem(UUID playerUUID) {
            Table.Factory retVal = Table.builder()
                    .columnOne("Item", Icon.called("user-shield").of(Family.SOLID).build())
                    .columnTwo("Enchant", Icon.called("file").of(Family.SOLID).build())
                    .columnThree("Level", Icon.called("arrow-alt-circle-up").of(Family.SOLID).build());

            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                return retVal.build();
            }

            Set<GenericEnchantableItem> items = getItems(player);
            for (GenericEnchantableItem item : items) {
                for (Map.Entry<GenericEnchantment, Integer> kvp : item.getEnchantments().entrySet()) {
                    if (kvp.getKey().equals(durability) && kvp.getValue() <= 0) {
                        continue;
                    }
                    retVal.addRow(((ItemStack) item.getConcrete()).getType(), kvp.getKey().getFriendlyName(), kvp.getValue());
                }
            }
            return retVal.build();
        }

        private Set<GenericEnchantableItem> getItems(Player player) {
            Set<GenericEnchantableItem> retVal = new HashSet<>();

            Optional<ItemStack> mainHand = entityItemHandler.getItemInMainHand(player);
            mainHand.ifPresent(itemStack -> retVal.add(BukkitEnchantableItem.fromItemStack(itemStack)));
            Optional<ItemStack> offHand = entityItemHandler.getItemInOffHand(player);
            offHand.ifPresent(itemStack -> retVal.add(BukkitEnchantableItem.fromItemStack(itemStack)));

            Optional<EntityEquipment> equipment = Optional.ofNullable(player.getEquipment());
            if (!equipment.isPresent()) {
                return retVal;
            }

            retVal.add(BukkitEnchantableItem.fromItemStack(equipment.get().getHelmet()));
            retVal.add(BukkitEnchantableItem.fromItemStack(equipment.get().getChestplate()));
            retVal.add(BukkitEnchantableItem.fromItemStack(equipment.get().getLeggings()));
            retVal.add(BukkitEnchantableItem.fromItemStack(equipment.get().getBoots()));
            retVal.remove(null);
            return retVal;
        }

        public CallEvents[] callExtensionMethodsOn() { return events; }
    }
}
