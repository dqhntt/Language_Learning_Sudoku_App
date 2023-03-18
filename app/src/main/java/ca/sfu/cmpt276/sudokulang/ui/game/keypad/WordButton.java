package ca.sfu.cmpt276.sudokulang.ui.game.keypad;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A UI representation of a word button.
 */
public class WordButton extends com.google.android.material.button.MaterialButton {
    public WordButton(@NonNull Context context) {
        this(context, null);
    }

    public WordButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, com.google.android.material.R.attr.materialButtonOutlinedStyle);
        init();
    }

    private void init() {
        setId(generateViewId());
    }
}
