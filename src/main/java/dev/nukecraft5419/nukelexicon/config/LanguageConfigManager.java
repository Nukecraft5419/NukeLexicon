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
package dev.nukecraft5419.nukelexicon.config;

import dev.nukecraft5419.nukelexicon.NukeLexicon;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the Per-Player Multi-Language (i18n) system.
 * Loads YAML files from the locales/ folder and provides messages based on the client's language.
 */
public class LanguageConfigManager {

    private final Plugin plugin;
    private final Map<String, CustomConfig> locales = new HashMap<>();

    /**
     * Initializes the LanguageConfigManager and loads all available locales.
     *
     * @param plugin The main plugin instance.
     */
    public LanguageConfigManager(Plugin plugin) {
        this.plugin = plugin;
        loadLocales();
    }

    /**
     * Loads and updates all language files from the {@code locales/} folder.
     * Automatically extracts default languages from the JAR if they do not exist on disk.
     */
    public void loadLocales() {
        locales.clear();

        // 1. Load default languages natively supported by the plugin
        locales.put("en_US", new CustomConfig(plugin, "en_US.yml", "locales"));
        locales.put("it_IT", new CustomConfig(plugin, "it_IT.yml", "locales"));

        // 2. Dynamically load any other custom language files added by the server admin
        File localesFolder = new File(plugin.getDataFolder(), "locales");
        if (localesFolder.exists() && localesFolder.isDirectory()) {
            File[] files = localesFolder.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files != null) {
                for (File file : files) {
                    String langCode = file.getName().replace(".yml", "").toLowerCase();
                    if (!locales.containsKey(langCode)) {
                        locales.put(langCode, new CustomConfig(plugin, file.getName(), "locales"));
                    }
                }
            }
        }
    }

    /**
     * Retrieves the raw string from the correct YAML file, based on the player's client language.
     *
     * @param sender The recipient of the message (Player or Console).
     * @param path   The path to the message inside the YAML file (e.g., {@code "plugin.hello"}).
     * @return The raw, unformatted string from the language file.
     */
    public String getRawMessage(CommandSender sender, String path) {
        // Get the fallback language defined in config.yml (e.g., "en_us")
        String fallback = NukeLexicon.getInstance().getFallbackLanguage().toLowerCase();
        String locale = fallback;

        // If the sender is a player, read their client language directly from Minecraft
        if (sender instanceof Player player) {
            // getLocale() returns standard format strings like "it_it", "en_us", "es_es"
            locale = player.getLocale().toLowerCase();
        }

        // Find the correct language file. If the player's language doesn't exist, use the fallback.
        CustomConfig config = locales.getOrDefault(locale, locales.get(fallback));

        if (config == null) {
            return "<red>Missing locale file for: " + locale + "</red>";
        }

        // Retrieve the string from the file
        return config.getConfig().getString(path, "<red>Missing translation key: " + path + "</red>");
    }

    /**
     * Retrieves a list of raw strings from the correct YAML file, based on the player's client language.
     *
     * @param sender The recipient of the message (Player or Console).
     * @param path   The path to the string list inside the YAML file (e.g., {@code "plugin.help"}).
     * @return The list of raw, unformatted strings from the language file.
     */
    public List<String> getRawMessageList(CommandSender sender, String path) {
        String fallback = NukeLexicon.getInstance().getFallbackLanguage().toLowerCase();
        String locale = fallback;

        if (sender instanceof Player player) {
            locale = player.getLocale().toLowerCase();
        }

        CustomConfig config = locales.getOrDefault(locale, locales.get(fallback));

        if (config == null) {
            return List.of("<red>Missing locale file for: " + locale + "</red>");
        }

        return config.getConfig().getStringList(path);
    }
}
