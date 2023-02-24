package ca.sfu.cmpt276.sudokulang.ui;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Utility functions for working with UI elements.
 *
 * @cite <a href="https://stackoverflow.com/q/29664993">Convert among dp, px, and sp</a>
 */
public class UiUtil {
    public static int dpToPx(float dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                Resources.getSystem().getDisplayMetrics()
        ));
    }

    public static int pxToDp(float px) {
        return Math.round(px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static @NonNull View[][] transpose(@NonNull View[][] matrix) {
        final int nRows = matrix.length;
        final int nCols = matrix[0].length;
        var transposedMatrix = new View[nCols][nRows];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return transposedMatrix;
    }
}
