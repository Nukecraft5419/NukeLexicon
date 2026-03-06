/*
 * MIT License
 *
 * Copyright (c) 2026 Nukecraft5419
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.nukecraft5419.nukelexicon;

import dev.nukecraft5419.nukelexicon.config.LanguageConfigManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.Plugin;

/**
 * NukeLexicon - Advanced i18n & MiniMessage Text API.
 * This is the main core class of the library.
 */
public class NukeLexicon {

    private static NukeLexicon instance;

    private final Plugin plugin;
    private Metrics metrics;
    private BukkitAudiences adventure;
    private final LanguageConfigManager languageManager;
    private final String fallbackLanguage;
    private final String defaultPrefix;

    /**
     * Private constructor for the Singleton pattern.
     */
    private NukeLexicon(Plugin plugin, String fallbackLanguage, String defaultPrefix) {
        this.plugin = plugin;
        this.metrics = new Metrics(plugin, 29948);
        this.fallbackLanguage = fallbackLanguage;
        this.defaultPrefix = defaultPrefix;

        // Initialize Adventure (MiniMessage) hooking into the user's plugin
        this.adventure = BukkitAudiences.create(plugin);

        // Initialize the language manager
        this.languageManager = new LanguageConfigManager(plugin);

        metrics.addCustomChart(new SimplePie("online_language", () -> fallbackLanguage));
    }

    /**
     * Initializes the NukeLexicon library with automatic metadata detection.
     * It retrieves the version and prefix directly from the provided plugin instance.
     * Fallback language defaults to "en_US".
     *
     * @param plugin The instance of the user plugin (e.g., 'this').
     */
    public static void init(Plugin plugin) {
        // Auto-detect version from plugin.yml
        String version = plugin.getDescription().getVersion();

        // Auto-detect prefix or use a formatted plugin name as fallback
        String prefix = plugin.getDescription().getPrefix() != null ?
            plugin.getDescription().getPrefix() :
            "&8[&b" + plugin.getName() + "&8]";

        // Call the main init method with default US English as fallback
        init(plugin, "en_US", prefix);
    }

    /**
     * Initializes the NukeLexicon library.
     * This must be called inside the user plugin's onEnable() method.
     *
     * @param plugin The instance of the user plugin (e.g., 'this').
     * @param fallbackLanguage The default fallback language (e.g., "en_US").
     * @param defaultPrefix The emergency prefix if missing in config.
     */
    public static void init(Plugin plugin, String fallbackLanguage, String defaultPrefix) {
        if (instance != null) {
            plugin.getLogger().warning("NukeLexicon is already initialized!");
            return;
        }
        instance = new NukeLexicon(plugin, fallbackLanguage, defaultPrefix);
    }

    /**
     * Closes the Adventure audiences to prevent memory leaks.
     * This should be called inside the user plugin's onDisable() method.
     */
    public static void close() {
        if (instance != null && instance.adventure != null) {
            instance.adventure.close();
            instance.adventure = null;
        }
        instance = null;
    }

    /**
     * Gets the global NukeLexicon instance.
     *
     * @return The NukeLexicon API instance.
     * @throws IllegalStateException If the library hasn't been initialized yet.
     */
    public static NukeLexicon getInstance() {
        if (instance == null) {
            throw new IllegalStateException("NukeLexicon is not initialized! Call NukeLexicon.init(...) first.");
        }
        return instance;
    }

    // --- GETTERS ---

    public Plugin getPlugin() {
        return plugin;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public LanguageConfigManager getLanguageManager() {
        return languageManager;
    }

    public String getFallbackLanguage() {
        return fallbackLanguage;
    }

    public String getDefaultPrefix() {
        return defaultPrefix;
    }
}
