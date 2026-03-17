package dev.nukecraft5419.nukelexicon.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to format time and durations into human-readable strings.
 */
public class TimeUtils {

    private TimeUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Formats seconds into a digital clock format.
     * If the time is under an hour, it returns MM:SS (e.g., "02:10").
     * If the time is an hour or more, it returns HH:MM:SS (e.g., "01:05:30").
     *
     * @param totalSeconds The amount of seconds to format.
     * @return The formatted digital string.
     */
    public static String formatDigital(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    /**
     * Formats seconds into a short text format (e.g., 130 -> "2m 10s").
     * Perfect for chat messages and cooldowns.
     *
     * @param totalSeconds The amount of seconds to format.
     * @return The formatted short string.
     */
    public static String formatShort(int totalSeconds) {
        if (totalSeconds <= 0) return "0s";

        int days = totalSeconds / 86400;
        int hours = (totalSeconds % 86400) / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0 || sb.isEmpty()) sb.append(seconds).append("s");

        return sb.toString().trim();
    }

    /**
     * Formats seconds into a full text format (e.g., 130 -> "2 minutes, 10 seconds").
     *
     * @param totalSeconds The amount of seconds to format.
     * @return The formatted full string.
     */
    public static String formatFull(int totalSeconds) {
        if (totalSeconds <= 0) return "0 seconds";

        int days = totalSeconds / 86400;
        int hours = (totalSeconds % 86400) / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append(days == 1 ? " day, " : " days, ");
        if (hours > 0) sb.append(hours).append(hours == 1 ? " hour, " : " hours, ");
        if (minutes > 0) sb.append(minutes).append(minutes == 1 ? " minute, " : " minutes, ");
        if (seconds > 0 || sb.isEmpty()) sb.append(seconds).append(seconds == 1 ? " second" : " seconds");

        String result = sb.toString().trim();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Formats a Unix timestamp (milliseconds) into a readable date string.
     *
     * @param millis The timestamp in milliseconds.
     * @param format The date format (e.g., "dd/MM/yyyy HH:mm").
     * @return The formatted date string.
     */
    public static String formatDate(long millis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(millis));
    }

    /**
     * Calculates the remaining seconds of a cooldown.
     *
     * @param lastUseMillis The timestamp (in milliseconds) when the action was last used.
     * @param cooldownSeconds The total cooldown time required in seconds.
     * @return The remaining seconds. If the result is {@code <=} 0, the cooldown is over.
     */
    public static int getCooldownLeft(long lastUseMillis, int cooldownSeconds) {
        long secondsPassed = (System.currentTimeMillis() - lastUseMillis) / 1000;
        return cooldownSeconds - (int) secondsPassed;
    }

    /**
     * Parses a duration string (e.g., "1d2h30m", "10m", "1h 30m") into total seconds.
     *
     * @param input The duration string to parse.
     * @return The total duration in seconds.
     */
    public static int parseDuration(String input) {
        if (input == null || input.isEmpty()) return 0;

        int totalSeconds = 0;
        input = input.toLowerCase().replaceAll("\\s+", "");

        Pattern pattern = Pattern.compile("(\\d+)([dhms])");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "d": totalSeconds += value * 86400; break;
                case "h": totalSeconds += value * 3600; break;
                case "m": totalSeconds += value * 60; break;
                case "s": totalSeconds += value; break;
            }
        }
        return totalSeconds;
    }
}
