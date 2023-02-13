package ca.sfu.cmpt276.sudokulang.ui;

import android.content.res.Resources;

/**
 * Utility functions for working with UI elements.
 *
 * @cite <a href="https://stackoverflow.com/a/34763668">Convert dp to px</a>
 */
public class Util {
    public static int dpToPx(float dp) {
        return Math.round(dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(float px) {
        return Math.round(px / Resources.getSystem().getDisplayMetrics().density);
    }
}
