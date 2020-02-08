package me.egg82.tcpp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.xml.xpath.XPathExpressionException;
import me.egg82.tcpp.utils.BukkitEnvironmentUtil;
import me.egg82.tcpp.utils.LogUtil;
import ninja.egg82.maven.Artifact;
import ninja.egg82.maven.Repository;
import ninja.egg82.maven.Scope;
import ninja.egg82.services.ProxiedURLClassLoader;
import ninja.egg82.utils.InjectUtil;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class BukkitBootstrap extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Object concrete;
    private Class<?> concreteClass;

    private final boolean isBukkit;

    private URLClassLoader proxiedClassLoader;
    private final ExecutorService downloadPool = Executors.newWorkStealingPool(Math.max(4, Runtime.getRuntime().availableProcessors()));

    public BukkitBootstrap() {
        super();
        isBukkit = BukkitEnvironmentUtil.getEnvironment() == BukkitEnvironmentUtil.Environment.BUKKIT;
    }

    @Override
    public void onLoad() {
        proxiedClassLoader = new ProxiedURLClassLoader(getClass().getClassLoader(), new String[] { "org\\.slf4j\\..*" });

        try {
            loadJars(new File(getDataFolder(), "external"), proxiedClassLoader);
        } catch (ClassCastException | IOException | IllegalAccessException | InvocationTargetException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Could not load required deps.");
        }

        downloadPool.shutdown();
        try {
            if (!downloadPool.awaitTermination(1L, TimeUnit.HOURS)) {
                logger.error("Could not download all dependencies. Please try again later.");
                return;
            }
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }

        try {
            concreteClass = proxiedClassLoader.loadClass("me.egg82.tcpp.TrollCommandsPlusPlus");
            concrete = concreteClass.getDeclaredConstructor(Plugin.class).newInstance(this);
            concreteClass.getMethod("onLoad").invoke(concrete);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Could not create main class.");
        }
    }

    @Override
    public void onEnable() {
        try {
            concreteClass.getMethod("onEnable").invoke(concrete);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Could not invoke onEnable.");
        }

        if (ExternalAPI.getInstance() == null) {
            ExternalAPI.setInstance(proxiedClassLoader);
        }
    }

    @Override
    public void onDisable() {
        try {
            concreteClass.getMethod("onDisable").invoke(concrete);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Could not invoke onDisable.");
        }
    }

    private void loadJars(File jarsDir, URLClassLoader classLoader) throws IOException, IllegalAccessException, InvocationTargetException {
        if (jarsDir.exists() && !jarsDir.isDirectory()) {
            Files.delete(jarsDir.toPath());
        }
        if (!jarsDir.exists()) {
            if (!jarsDir.mkdirs()) {
                throw new IOException("Could not create parent directory structure.");
            }
        }

        File cacheDir = new File(jarsDir, "cache");

        // First

        Artifact.Builder guava = Artifact.builder("com.google.guava", "guava", "28.2-jre", cacheDir)
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInjectWait(guava, jarsDir, classLoader, "Google Guava", 1);

        // Same file

        InjectUtil.injectFile(getFile(), classLoader);

        // Local

        Artifact.Builder taskchainBukkit = Artifact.builder("co.aikar", "taskchain-bukkit", "3.7.2", cacheDir)
                .addRepository(Repository.builder("https://repo.aikar.co/nexus/content/groups/aikar/").addProxy("https://nexus.egg82.me/repository/aikar/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(taskchainBukkit, jarsDir, classLoader, "Taskchain", 1);

        printLatest("ACF");
        Artifact.Builder acfPaper = Artifact.builder("co.aikar", "acf-paper", "0.5.0-SNAPSHOT", cacheDir)
                .addDirectJarURL("https://nexus.egg82.me/repository/aikar/{GROUP}/{ARTIFACT}/{VERSION}/{ARTIFACT}-{SNAPSHOT}-shaded.jar")
                .addDirectJarURL("https://repo.aikar.co/nexus/content/groups/aikar/{GROUP}/{ARTIFACT}/{VERSION}/{ARTIFACT}-{SNAPSHOT}-shaded.jar")
                .addRepository(Repository.builder("https://repo.aikar.co/nexus/content/groups/aikar/").addProxy("https://nexus.egg82.me/repository/aikar/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(acfPaper, jarsDir, classLoader, "ACF");

        Artifact.Builder eventChainBukkit = Artifact.builder("ninja.egg82", "event-chain-bukkit", "1.0.9", cacheDir)
                .addRepository(Repository.builder("https://www.myget.org/F/egg82-java/maven/").addProxy("https://nexus.egg82.me/repository/egg82/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(eventChainBukkit, jarsDir, classLoader, "Event Chain");

        Artifact.Builder configurateYaml = Artifact.builder("org.spongepowered", "configurate-yaml", "3.6.1", cacheDir)
                .addRepository(Repository.builder("https://repo.spongepowered.org/maven/").addProxy("https://nexus.egg82.me/repository/sponge/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(configurateYaml, jarsDir, classLoader, "Configurate", 2);

        Artifact.Builder spigotUpdater = Artifact.builder("ninja.egg82", "spigot-updater", "1.0.4", cacheDir)
                .addRepository(Repository.builder("https://www.myget.org/F/egg82-java/maven/").addProxy("https://nexus.egg82.me/repository/egg82/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(spigotUpdater, jarsDir, classLoader, "Spigot Updater");

        Artifact.Builder serviceLocator = Artifact.builder("ninja.egg82", "service-locator", "1.0.1", cacheDir)
                .addRepository(Repository.builder("https://www.myget.org/F/egg82-java/maven/").addProxy("https://nexus.egg82.me/repository/egg82/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(serviceLocator, jarsDir, classLoader, "Service Locator");

        Artifact.Builder javassist = Artifact.builder("org.javassist", "javassist", "3.26.0-GA", cacheDir)
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(javassist, jarsDir, classLoader, "Javassist");

        Artifact.Builder javaxAnnotationApi = Artifact.builder("javax.annotation", "javax.annotation-api", "1.3.2", cacheDir)
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(javaxAnnotationApi, jarsDir, classLoader, "Javax Annotations");

        Artifact.Builder reflectionUtils = Artifact.builder("ninja.egg82", "reflection-utils", "1.0.4", cacheDir)
                .addRepository(Repository.builder("https://www.myget.org/F/egg82-java/maven/").addProxy("https://nexus.egg82.me/repository/egg82/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(reflectionUtils, jarsDir, classLoader, "Reflection Utils");

        Artifact.Builder mineskinJavaClient = Artifact.builder("org.mineskin", "java-client", "1.0.3-SNAPSHOT", cacheDir)
                .addRepository(Repository.builder("https://repo.inventivetalent.org/content/groups/public/").addProxy("https://nexus.egg82.me/repository/inventivetalent/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(mineskinJavaClient, jarsDir, classLoader, "Mineskin Client");

        printLatest("PacketWrapper");
        Artifact.Builder packetWrapper = Artifact.builder("com.comphenix.packetwrapper", "PacketWrapper", "1.13-R0.1-SNAPSHOT", cacheDir)
                .addRepository(Repository.builder("http://repo.dmulloy2.net/nexus/repository/snapshots/").addProxy("https://nexus.egg82.me/repository/dmulloy2-snapshots/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(packetWrapper, jarsDir, classLoader, "PacketWrapper");

        printLatest("EffectLib");
        Artifact.Builder effectLib = Artifact.builder("de.slikey", "EffectLib", "6.2", cacheDir)
                .addRepository(Repository.builder("http://maven.elmakers.com/repository/").addProxy("https://nexus.egg82.me/repository/elmakers/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(effectLib, jarsDir, classLoader, "EffectLib");

        // Global

        Artifact.Builder caffeine = Artifact.builder("com.github.ben-manes.caffeine", "caffeine", "2.8.0", cacheDir)
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(caffeine, jarsDir, classLoader, "Caffeine");

        Artifact.Builder gameanalyticsApi = Artifact.builder("ninja.egg82", "gameanalytics-api", "1.0.1", cacheDir)
                .addRepository(Repository.builder("https://www.myget.org/F/egg82-java/maven/").addProxy("https://nexus.egg82.me/repository/egg82/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(gameanalyticsApi, jarsDir, classLoader, "GameAnalytics API", 1);

        Artifact.Builder abstractConfiguration = Artifact.builder("ninja.egg82", "abstract-configuration", "1.0.1", cacheDir)
                .addRepository(Repository.builder("https://www.myget.org/F/egg82-java/maven/").addProxy("https://nexus.egg82.me/repository/egg82/").build())
                .addRepository(Repository.builder("https://repo1.maven.org/maven2/").addProxy("https://nexus.egg82.me/repository/maven-central/").build());
        buildInject(abstractConfiguration, jarsDir, classLoader, "Abstract Configuration");
    }

    private void printLatest(String friendlyName) {
        log(Level.INFO, LogUtil.getHeading() + ChatColor.YELLOW + "Checking version of " + ChatColor.WHITE + friendlyName);
    }

    private void buildInject(Artifact.Builder builder, File jarsDir, URLClassLoader classLoader, String friendlyName) {
        buildInject(builder, jarsDir, classLoader, friendlyName, 0);
    }

    private void buildInject(Artifact.Builder builder, File jarsDir, URLClassLoader classLoader, String friendlyName, int depth) {
        downloadPool.submit(() -> buildInjectWait(builder, jarsDir, classLoader, friendlyName, depth));
    }

    private void buildInjectWait(Artifact.Builder builder, File jarsDir, URLClassLoader classLoader, String friendlyName, int depth) {
        Exception lastEx = null;
        try {
            injectArtifact(builder.build(), jarsDir, classLoader, friendlyName, depth);
            return;
        } catch (IOException ex) {
            lastEx = ex;
        } catch (IllegalAccessException | InvocationTargetException | URISyntaxException | XPathExpressionException | SAXException ex) {
            logger.error(ex.getMessage(), ex);
            return;
        }

        if (depth > 0) {
            logger.error(lastEx.getMessage(), lastEx);
            return;
        }

        try {
            injectArtifact(builder, jarsDir, classLoader);
        } catch (IOException | IllegalAccessException | InvocationTargetException ex) {
            logger.error(lastEx.getMessage(), lastEx);
        }
    }

    private void injectArtifact(Artifact artifact, File jarsDir, URLClassLoader classLoader, String friendlyName, int depth) throws IOException, IllegalAccessException, InvocationTargetException, URISyntaxException, XPathExpressionException, SAXException {
        File output = new File(jarsDir, artifact.getGroupId()
                + "-" + artifact.getArtifactId()
                + "-" + artifact.getRealVersion() + ".jar"
        );

        if (friendlyName != null && !artifact.fileExists(output)) {
            log(Level.INFO, LogUtil.getHeading() + ChatColor.YELLOW + "Downloading " + ChatColor.WHITE + friendlyName);
        }
        artifact.injectJar(output, classLoader);

        if (depth > 0) {
            for (Artifact dependency : artifact.getDependencies()) {
                if (dependency.getScope() == Scope.COMPILE || dependency.getScope() == Scope.RUNTIME) {
                    injectArtifact(dependency, jarsDir, classLoader, null, depth - 1);
                }
            }
        }
    }

    private void injectArtifact(Artifact.Builder builder, File jarsDir, URLClassLoader classLoader) throws IOException, IllegalAccessException, InvocationTargetException {
        File[] files = jarsDir.listFiles();
        if (files == null) {
            throw new IOException();
        }

        long latest = Long.MIN_VALUE;
        File retVal = null;
        for (File file : files) {
            if (file.getName().startsWith(builder.getGroupId() + "-" + builder.getArtifactId()) && file.lastModified() >= latest) {
                latest = file.lastModified();
                retVal = file;
            }
        }

        if (retVal == null) {
            throw new IOException();
        }
        InjectUtil.injectFile(retVal, classLoader);
    }

    private void log(Level level, String message) {
        getServer().getLogger().log(level, (isBukkit) ? ChatColor.stripColor(message) : message);
    }
}
