package ca.sfu.cmpt276.sudokulang.ui;

import android.content.Context;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Locale;

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

    @NonNull
    public static TextToSpeech makeTts(@NonNull Context context, @NonNull Locale locale) {
        return new LocaleTTS(context, locale).getTts();
    }

    private static class LocaleTTS implements TextToSpeech.OnInitListener {
        private final Locale mLocale;
        private final TextToSpeech mTts;

        public LocaleTTS(@NonNull Context context, @NonNull Locale locale) {
            mLocale = locale;
            mTts = new TextToSpeech(context, this);
        }

        @NonNull
        public TextToSpeech getTts() {
            return mTts;
        }

        // Cite: https://stackoverflow.com/a/21354040
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                final int result = mTts.setLanguage(mLocale);
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(getClass().getTypeName(), "This Language is not supported");
                }
            } else {
                Log.e(getClass().getTypeName(), "Initialization Failed!");
            }
        }
    }
}
