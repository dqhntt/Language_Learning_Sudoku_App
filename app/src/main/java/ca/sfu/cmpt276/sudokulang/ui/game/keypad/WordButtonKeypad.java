package ca.sfu.cmpt276.sudokulang.ui.game.keypad;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Arrays;

/**
 * A UI representation of a group of word buttons.
 */
public class WordButtonKeypad extends ConstraintLayout {
    private WordButton[] mButtons;

    public WordButtonKeypad(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setValues(@NonNull String[] buttonValues) {
        if (mButtons == null || mButtons.length != buttonValues.length) {
            createNewButtons(buttonValues.length);
        }
        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setText(buttonValues[i]);
        }
    }

    private void createNewButtons(int n) {
        assert (n > 0);
        mButtons = new WordButton[n];
        // Create and add buttons to layout.
        for (int i = 0; i < n; i++) {
            final var button = new WordButton(getContext());
            mButtons[i] = button;
            addView(button);
        }
        constrainButtonsInLayout(n == 4 ? 2 : 3);
    }

    // See: https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintSet
    private void constrainButtonsInLayout(int nMaxButtonsPerRow) {
        final var constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        final @IdRes int[] buttonIds = Arrays.stream(mButtons).mapToInt(View::getId).toArray();
        chainAndAlignButtonsHorizontally(buttonIds, nMaxButtonsPerRow, constraintSet);
        chainButtonsInFirstColumn(buttonIds, nMaxButtonsPerRow, constraintSet);

        constraintSet.applyTo(this);
    }

    private void chainAndAlignButtonsHorizontally(@IdRes int[] buttonIds, int nMaxButtonsPerRow,
                                                  @NonNull ConstraintSet constraintSet) {
        // Chain and align all but the last button horizontally.
        for (int i = 0; i < mButtons.length - 1; i++) {
            // Firsts in rows connect to start of parent.
            if (i % nMaxButtonsPerRow == 0) {
                constraintSet.connect(
                        buttonIds[i], ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);
            }
            // Lasts in full rows connect to end of parent.
            else if ((i + 1) % nMaxButtonsPerRow == 0) {
                constraintSet.connect(
                        buttonIds[i], ConstraintSet.END,
                        ConstraintSet.PARENT_ID, ConstraintSet.END);
                // Chain with left button.
                chain2ViewsHorizontally(buttonIds[i - 1], buttonIds[i], constraintSet);
                // Align with left button.
                alignViewsRightWithLeft(buttonIds[i - 1], buttonIds[i], constraintSet);
            } else {
                // Chain with left and right buttons.
                chain2ViewsHorizontally(buttonIds[i - 1], buttonIds[i], constraintSet);
                chain2ViewsHorizontally(buttonIds[i], buttonIds[i + 1], constraintSet);
                // Align buttons.
                alignViewsRightWithLeft(buttonIds[i - 1], buttonIds[i], constraintSet);
                alignViewsRightWithLeft(buttonIds[i], buttonIds[i + 1], constraintSet);
            }
        }
        // Arrange the last button.
        if (mButtons.length % nMaxButtonsPerRow == 1) {
            // Center if it's the only one in that row.
            constraintSet.centerHorizontally(buttonIds[mButtons.length - 1], ConstraintSet.PARENT_ID);
        } else {
            final @IdRes int leftId = buttonIds[mButtons.length - 2];
            final @IdRes int lastButtonId = buttonIds[mButtons.length - 1];
            // Chain and align with left button.
            chain2ViewsHorizontally(leftId, lastButtonId, constraintSet);
            alignViewsRightWithLeft(leftId, lastButtonId, constraintSet);
            // Connect to end of parent.
            constraintSet.connect(
                    lastButtonId, ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        }
    }

    private void chainButtonsInFirstColumn(@IdRes int[] buttonIds, int nMaxButtonsPerRow,
                                           @NonNull ConstraintSet constraintSet) {
        final int nRows = mButtons.length % nMaxButtonsPerRow == 0
                ? mButtons.length / nMaxButtonsPerRow
                : mButtons.length / nMaxButtonsPerRow + 1;
        if (nRows > 1) {
            final @IdRes int[] firstColIds = new int[nRows];
            for (int i = 0; i < nRows; i++) {
                firstColIds[i] = buttonIds[i * nMaxButtonsPerRow];
            }
            constraintSet.createVerticalChain(
                    ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                    firstColIds, null, ConstraintSet.CHAIN_SPREAD_INSIDE);
        } else {
            constraintSet.centerVertically(buttonIds[0], ConstraintSet.PARENT_ID);
        }
    }

    private void chain2ViewsHorizontally(@IdRes int leftId, @IdRes int rightId,
                                         @NonNull ConstraintSet constraintSet) {
        constraintSet.connect(leftId, ConstraintSet.END, rightId, ConstraintSet.START);
        constraintSet.connect(rightId, ConstraintSet.START, leftId, ConstraintSet.END);
    }

    private void alignViewsRightWithLeft(@IdRes int leftId, @IdRes int rightId,
                                         @NonNull ConstraintSet constraintSet) {
        constraintSet.connect(rightId, ConstraintSet.TOP, leftId, ConstraintSet.TOP);
        constraintSet.connect(rightId, ConstraintSet.BOTTOM, leftId, ConstraintSet.BOTTOM);
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
