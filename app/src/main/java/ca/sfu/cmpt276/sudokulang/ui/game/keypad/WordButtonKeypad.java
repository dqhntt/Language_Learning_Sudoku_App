package ca.sfu.cmpt276.sudokulang.ui.game.keypad;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Arrays;

import ca.sfu.cmpt276.sudokulang.R;

/**
 * A UI representation of a group of word buttons.
 */
public class WordButtonKeypad extends androidx.constraintlayout.helper.widget.Flow {
    private final int mTextSizePx;
    private WordButton[] mButtons;

    // Cite: https://developer.android.com/develop/ui/views/layout/custom-views/create-view
    public WordButtonKeypad(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.WordButtonKeypad, 0, 0);
        try {
            mTextSizePx = a.getDimensionPixelSize(R.styleable.WordButtonKeypad_buttonTextSize, -1);
        } finally {
            a.recycle();
        }
    }

    /**
     * @implNote This function creates new buttons, thus clearing all {@link View#OnClickListener} present.
     */
    public void setValues(@NonNull String[] buttonValues) {
        if (mButtons == null || mButtons.length != buttonValues.length) {
            removeAllButtons();
            createNewButtons(buttonValues.length);
        }
        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setText(buttonValues[i]);
        }
    }

    private void removeAllButtons() {
        if (mButtons != null) {
            for (var button : mButtons) {
                ((ConstraintLayout) getParent()).removeView(button);
            }
        }
    }

    private void createNewButtons(int n) {
        assert (n > 0);
        mButtons = new WordButton[n];
        // Create and add buttons to parent ConstraintLayout.
        for (int i = 0; i < n; i++) {
            final var button = new WordButton(getContext());
            if (mTextSizePx != -1) {
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizePx);
            }
            mButtons[i] = button;
            ((ConstraintLayout) getParent()).addView(button);
        }
        setReferencedIds(Arrays.stream(mButtons).mapToInt(View::getId).toArray());
    }

    /**
     * Register a callback to be invoked when each word button is clicked.
     *
     * @param l The callback that will run.
     */
    public void setOnclickListenersForAllButtons(OnClickListener l) {
        for (var button : mButtons) {
            button.setOnClickListener(l);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (var button : mButtons) {
            button.setEnabled(enabled);
        }
    }
}