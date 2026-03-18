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
package dev.nukecraft5419.nukelexicon.utils;

import dev.nukecraft5419.nukelexicon.NukeLexicon;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.List;

/**
 * Utility class for delivering formatted messages to players and console.
 * Utilizes Adventure's BukkitAudiences to safely send modern Components on Spigot servers.
 */
public class SendUtils {

    private SendUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // =========================================
    // FULL METHODS (With TagResolver parameter)
    // =========================================

    /**
     * Formats and sends a single MiniMessage string to a CommandSender.
     *
     * @param sender    The recipient (Player or Console).
     * @param message   The raw string containing MiniMessage tags.
     * @param extraTags Optional custom Placeholder tags to resolve (use TagResolver.empty() if none).
     */
    public static void sendMessage(CommandSender sender, String message, TagResolver extraTags) {
        if (sender == null || message == null || message.isEmpty()) return;

        // 1. Convert the raw string into an Adventure Component using our format engine
        Component component = MessagesUtils.format(sender, message, extraTags);

        // 2. Deliver the Component using the Adventure Platform bridge.
        // This is necessary because native Spigot CommandSenders do not support Component objects directly.
        NukeLexicon.getInstance().getAdventure().sender(sender).sendMessage(component);
    }

    /**
     * Formats and sends a list of MiniMessage strings to a CommandSender.
     *
     * @param sender    The recipient (Player or Console).
     * @param messages  The list of raw strings to send.
     * @param extraTags Optional custom Placeholder tags to resolve (use TagResolver.empty() if none).
     */
    public static void sendMessages(CommandSender sender, List<String> messages, TagResolver extraTags) {
        if (sender == null || messages == null || messages.isEmpty()) return;

        for (String msg : messages) {
            sendMessage(sender, msg, extraTags);
        }
    }

    /**
     * Translates and sends a single message to a CommandSender based on their client language.
     * Automatically retrieves the string from the locales folder and applies formatting.
     *
     * @param sender    The recipient of the message (Player or Console).
     * @param path      The YAML key path in the locale file (e.g., "messages.welcome").
     * @param extraTags Additional custom Placeholder tags to resolve (can be TagResolver.empty()).
     */
    public static void sendTranslation(CommandSender sender, String path, TagResolver extraTags) {
        if (sender == null || path == null || path.isEmpty()) return;

        String rawMessage = NukeLexicon.getInstance().getLanguageManager().getRawMessage(sender, path);

        if (rawMessage == null || rawMessage.isEmpty()) return;

        sendMessage(sender, rawMessage, extraTags);
    }

    /**
     * Translates and sends a list of messages to a CommandSender based on their client language.
     * Automatically retrieves the correct string list from the locales folder and applies
     * MiniMessage formatting and PlaceholderAPI parsing.
     *
     * @param sender    The recipient of the messages (Player or Console).
     * @param path      The YAML key path to the string list in the locales file (e.g., "plugin.help").
     * @param extraTags Optional custom Placeholder tags to resolve (use TagResolver.empty() if none).
     */
    public static void sendTranslations(CommandSender sender, String path, TagResolver extraTags) {
        // 1. Validate inputs to prevent errors
        if (sender == null || path == null || path.isEmpty()) return;

        // 2. Retrieve the raw list of strings tailored to the player's language
        List<String> rawMessages = NukeLexicon.getInstance().getLanguageManager().getRawMessageList(sender, path);

        // 3. If the list is missing or empty in the config, silently abort
        if (rawMessages == null || rawMessages.isEmpty()) return;

        // 4. Delegate to the existing list sender (which handles formatting and dispatching)
        sendMessages(sender, rawMessages, extraTags);
    }

    /**
     * Shorthand to send a formatted message directly to the server console.
     * Useful for startup, shutdown, or debugging logs.
     *
     * @param message   The message to log (supports custom tags like {@code <prefix>} and colors like {@code <green>}).
     * @param extraTags Optional custom Placeholder tags to resolve (use TagResolver.empty() if none).
     */
    public static void log(String message, TagResolver extraTags) {
        sendMessage(Bukkit.getConsoleSender(), message, extraTags);
    }

    /**
     * Sends a translated Action Bar message to a specific player with custom tags.
     *
     * @param player    The player who will receive the Action Bar message.
     * @param path      The path of the message in the locale configuration file.
     * @param extraTags Optional custom Placeholder tags to resolve (use TagResolver.empty() if none).
     */
    public static void sendActionBar(@NonNull Player player, @NonNull String path, TagResolver extraTags) {
        String rawText = NukeLexicon.getInstance().getLanguageManager().getRawMessage(player, path);
        if (rawText == null || rawText.isEmpty()) return;

        Component component = MessagesUtils.format(player, rawText, extraTags);
        NukeLexicon.getInstance().getAdventure().player(player).sendActionBar(component);
    }

    /**
     * Sends a translated Title and Subtitle to a specific player with custom timings and custom tags.
     *
     * @param player       The player who will receive the Title.
     * @param titlePath    The path of the main title string in the locale configuration.
     * @param subtitlePath The path of the subtitle string in the locale configuration.
     * @param inMillis     The fade-in time in milliseconds.
     * @param stayMillis   The amount of time the title stays on screen in milliseconds.
     * @param outMillis    The fade-out time in milliseconds.
     * @param extraTags    Optional custom Placeholder tags to resolve (use TagResolver.empty() if none).
     */
    public static void sendTitle(@NonNull Player player, @NonNull String titlePath, @NonNull String subtitlePath, int inMillis, int stayMillis, int outMillis, TagResolver extraTags) {
        String rawTitle = NukeLexicon.getInstance().getLanguageManager().getRawMessage(player, titlePath);
        String rawSubtitle = NukeLexicon.getInstance().getLanguageManager().getRawMessage(player, subtitlePath);

        Component titleComp = rawTitle != null && !rawTitle.isEmpty() ? MessagesUtils.format(player, rawTitle, extraTags) : Component.empty();
        Component subComp = rawSubtitle != null && !rawSubtitle.isEmpty() ? MessagesUtils.format(player, rawSubtitle, extraTags) : Component.empty();

        Title.Times times = Title.Times.times(
            Duration.ofMillis(inMillis),
            Duration.ofMillis(stayMillis),
            Duration.ofMillis(outMillis)
        );

        Title title = Title.title(titleComp, subComp, times);
        NukeLexicon.getInstance().getAdventure().player(player).showTitle(title);
    }

    /**
     * Plays a Kyori Adventure sound for a specific player.
     *
     * @param player   The player who will hear the sound.
     * @param soundKey The Minecraft sound key (e.g., "entity.bat.takeoff").
     * @param volume   The volume of the sound (default is 1.0f).
     * @param pitch    The pitch of the sound (1.0f is normal, 0.8f is deeper, 1.2f is higher).
     */
    public static void playSound(@NonNull Player player, @NonNull String soundKey, float volume, float pitch) {
        Sound sound = Sound.sound(
            Key.key(soundKey),
            Sound.Source.MASTER,
            volume,
            pitch
        );
        NukeLexicon.getInstance().getAdventure().player(player).playSound(sound);
    }

    /**
     * Retrieves a translated string and parses it directly into a Kyori Adventure Component with custom tags.
     *
     * @param sender    The target audience (Player or Console) used to determine the locale.
     * @param path      The path of the message in the locale configuration file.
     * @param extraTags Optional custom Placeholder tags to resolve (use TagResolver.empty() if none).
     * @return The parsed Component ready to be sent or manipulated.
     */
    public static Component getTranslationComponent(@NonNull CommandSender sender, @NonNull String path, TagResolver extraTags) {
        String rawText = NukeLexicon.getInstance().getLanguageManager().getRawMessage(sender, path);
        if (rawText == null || rawText.isEmpty()) return Component.empty();

        return MessagesUtils.format(sender, rawText, extraTags);
    }

    /**
     * Opens a virtual book for a player with translated and formatted pages.
     *
     * @param player     The player who will see the book.
     * @param titlePath  The key path for the title in the language file.
     * @param author     The author of the book (e.g., Server Name).
     * @param pagesPath  The key path for the list of strings (pages) in the language file.
     * @param extraTags  Optional custom tags for placeholders.
     */
    public static void openBook(@NonNull Player player, @NonNull String titlePath, @NonNull String author, @NonNull String pagesPath, TagResolver extraTags) {
        // Retrieve the translated title as a Component
        Component title = getTranslationComponent(player, titlePath, extraTags);

        // Retrieve the raw list of pages from the language manager
        List<String> rawPages = NukeLexicon.getInstance().getLanguageManager().getRawMessageList(player, pagesPath);

        // Build the Adventure Book
        Book.Builder bookBuilder = Book.builder()
            .title(title)
            .author(Component.text(author));

        // Format each page with MiniMessage and add it to the book
        for (String page : rawPages) {
            bookBuilder.addPage(MessagesUtils.format(player, page, extraTags));
        }

        // Open the book for the player
        NukeLexicon.getInstance().getAdventure().player(player).openBook(bookBuilder.build());
    }

    /**
     * Creates and shows a translated BossBar to a player.
     *
     * @param player    The target player.
     * @param path      The path to the message in the locale file.
     * @param color     The color of the bar (e.g., BossBar.Color.PURPLE).
     * @param overlay   The overlay style (e.g., BossBar.Overlay.PROGRESS).
     * @param progress  The progress value (0.0f to 1.0f).
     * @param extraTags Optional custom tags to resolve.
     * @return The created BossBar instance, useful for updating or hiding it later.
     */
    public static BossBar showBossBar(@NonNull Player player, @NonNull String path, BossBar.Color color, BossBar.Overlay overlay, float progress, TagResolver extraTags) {
        Component name = getTranslationComponent(player, path, extraTags);
        BossBar bar = BossBar.bossBar(name, progress, color, overlay);
        NukeLexicon.getInstance().getAdventure().player(player).showBossBar(bar);
        return bar;
    }

    /**
     * Hides a specific BossBar from a player.
     *
     * @param player The target player.
     * @param bar    The BossBar instance to hide.
     */
    public static void hideBossBar(@NonNull Player player, @NonNull BossBar bar) {
        NukeLexicon.getInstance().getAdventure().player(player).hideBossBar(bar);
    }

    /**
     * Updates the text of an existing BossBar using a translated string.
     * Useful for minigames, timers, or updating stats on the fly.
     *
     * @param sender    The target audience (Player or Console) used to determine the locale.
     * @param bar       The existing BossBar to update.
     * @param path      The path to the new message in the locale file.
     * @param extraTags Optional custom tags to resolve.
     */
    public static void updateBossBarName(@NonNull CommandSender sender, @NonNull BossBar bar, @NonNull String path, TagResolver extraTags) {
        bar.name(getTranslationComponent(sender, path, extraTags));
    }

    // =========================================
    // SHORTHAND METHODS (No TagResolver needed)
    // =========================================

    /**
     * Formats and sends a single MiniMessage string to a CommandSender.
     *
     * @param sender  The recipient (Player or Console).
     * @param message The raw string containing MiniMessage tags.
     */
    public static void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, message, TagResolver.empty());
    }

    /**
     * Formats and sends a list of MiniMessage strings to a CommandSender.
     *
     * @param sender   The recipient (Player or Console).
     * @param messages The list of raw strings to send.
     */
    public static void sendMessages(CommandSender sender, List<String> messages) {
        sendMessages(sender, messages, TagResolver.empty());
    }

    /**
     * Translates and sends a single message to a CommandSender based on their client language.
     *
     * @param sender The recipient of the message (Player or Console).
     * @param path   The YAML key path in the locale file.
     */
    public static void sendTranslation(CommandSender sender, String path) {
        sendTranslation(sender, path, TagResolver.empty());
    }

    /**
     * Translates and sends a list of messages to a CommandSender based on their client language.
     *
     * @param sender The recipient of the messages (Player or Console).
     * @param path   The YAML key path to the string list in the locales file.
     */
    public static void sendTranslations(CommandSender sender, String path) {
        sendTranslations(sender, path, TagResolver.empty());
    }

    /**
     * Logs a formatted message directly to the server console.
     *
     * @param message The message to log.
     */
    public static void log(String message) {
        log(message, TagResolver.empty());
    }

    /**
     * Sends a translated Action Bar message to a specific player.
     *
     * @param player The player who will receive the Action Bar message.
     * @param path   The path of the message in the locale configuration file.
     */
    public static void sendActionBar(@NonNull Player player, @NonNull String path) {
        sendActionBar(player, path, TagResolver.empty());
    }

    /**
     * Sends a translated Title and Subtitle to a specific player with custom timings.
     * Uses no extra custom tags.
     *
     * @param player       The player who will receive the Title.
     * @param titlePath    The path of the main title string in the locale configuration.
     * @param subtitlePath The path of the subtitle string in the locale configuration.
     * @param inMillis     The fade-in time in milliseconds.
     * @param stayMillis   The amount of time the title stays on screen in milliseconds.
     * @param outMillis    The fade-out time in milliseconds.
     */
    public static void sendTitle(@NonNull Player player, @NonNull String titlePath, @NonNull String subtitlePath, int inMillis, int stayMillis, int outMillis) {
        sendTitle(player, titlePath, subtitlePath, inMillis, stayMillis, outMillis, TagResolver.empty());
    }

    /**
     * Sends a translated Title and Subtitle to a specific player with custom tags.
     * Uses default timings: 0.5s fade-in, 3.0s stay, 1.0s fade-out.
     *
     * @param player       The player who will receive the Title.
     * @param titlePath    The path of the main title string in the locale configuration.
     * @param subtitlePath The path of the subtitle string in the locale configuration.
     * @param extraTags    Custom Placeholder tags to resolve.
     */
    public static void sendTitle(@NonNull Player player, @NonNull String titlePath, @NonNull String subtitlePath, TagResolver extraTags) {
        sendTitle(player, titlePath, subtitlePath, 500, 3000, 1000, extraTags);
    }

    /**
     * Sends a translated Title and Subtitle to a specific player.
     * Uses default timings: 0.5s fade-in, 3.0s stay, 1.0s fade-out, and no custom tags.
     *
     * @param player       The player who will receive the Title.
     * @param titlePath    The path of the main title string in the locale configuration.
     * @param subtitlePath The path of the subtitle string in the locale configuration.
     */
    public static void sendTitle(@NonNull Player player, @NonNull String titlePath, @NonNull String subtitlePath) {
        sendTitle(player, titlePath, subtitlePath, 500, 3000, 1000, TagResolver.empty());
    }

    /**
     * Retrieves a translated string and parses it directly into a Kyori Adventure Component.
     *
     * @param sender The target audience (Player or Console) used to determine the locale.
     * @param path   The path of the message in the locale configuration file.
     * @return The parsed Component ready to be sent or manipulated.
     */
    public static Component getTranslationComponent(@NonNull CommandSender sender, @NonNull String path) {
        return getTranslationComponent(sender, path, TagResolver.empty());
    }

    /**
     * Shorthand to open a virtual book without extra tags.
     *
     * @param player     The player who will see the book.
     * @param titlePath  The key path for the title in the language file.
     * @param author     The author of the book.
     * @param pagesPath  The key path for the pages in the language file.
     */
    public static void openBook(@NonNull Player player, @NonNull String titlePath, @NonNull String author, @NonNull String pagesPath) {
        openBook(player, titlePath, author, pagesPath, TagResolver.empty());
    }

    /**
     * Creates and shows a translated BossBar to a player without extra tags.
     *
     * @param player    The target player.
     * @param path      The path to the message in the locale file.
     * @param color     The color of the bar.
     * @param overlay   The overlay style.
     * @param progress  The progress value (0.0f to 1.0f).
     * @return The created BossBar instance.
     */
    public static BossBar showBossBar(@NonNull Player player, @NonNull String path, BossBar.Color color, BossBar.Overlay overlay, float progress) {
        return showBossBar(player, path, color, overlay, progress, TagResolver.empty());
    }

    /**
     * Updates the text of an existing BossBar using a translated string without extra tags.
     *
     * @param sender The target audience used to determine the locale.
     * @param bar    The existing BossBar to update.
     * @param path   The path to the new message in the locale file.
     */
    public static void updateBossBarName(@NonNull CommandSender sender, @NonNull BossBar bar, @NonNull String path) {
        updateBossBarName(sender, bar, path, TagResolver.empty());
    }
}
