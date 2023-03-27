package ca.sfu.cmpt276.sudokulang;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Util {
    @NonNull
    public static String formatWithTime(@NonNull String text, long timeInMs) {
        long minutes = (timeInMs / 1000) / 60;
        long seconds = (timeInMs / 1000) % 60;
        return String.format(Locale.getDefault(), text, minutes, seconds);
    }
}