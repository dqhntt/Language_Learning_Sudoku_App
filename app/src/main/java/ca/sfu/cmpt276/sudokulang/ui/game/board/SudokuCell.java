package ca.sfu.cmpt276.sudokulang.ui.game.board;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.ui.Util;

public class SudokuCell extends TextView {
    private int mRowIndex, mColIndex;
    private boolean mIsPrefilled;
    private State mState;

    public SudokuCell(@NonNull Context context) {
        super(context);
        init();
    }

    public SudokuCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Make it square.
        var layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        layoutParams.dimensionRatio = "1:1";

        // Make it fill all available layout space, considering other weights.
        layoutParams.horizontalWeight = layoutParams.verticalWeight = 1f;
        setLayoutParams(layoutParams);

        // Make text stay 2dp away from borders.
        final int padding = Util.dpToPx(2);
        setPadding(padding, padding, padding, padding);

        setGravity(Gravity.CENTER);
        setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelSmall);

        setProperties(true, State.NORMAL, "Cell");
        setRowIndex(-1);
        setColIndex(-1);
//        setId(generateViewId());
    }

    public void setProperties(boolean prefilled, State state, String text) {
        setPrefilled(prefilled);
        setState(state);
        setText(text);
    }

    public State getState() {
        return mState;
    }

    /**
     * Set the background color and cell state to the provided argument.
     *
     * @param state Game state of the cell.
     * @cite <a href="https://stackoverflow.com/a/29445079">Put border around TextView with a shape drawable</a>
     */
    public void setState(State state) {
        mState = state;
        switch (state) {
            case NORMAL:
                setBackgroundResource(R.drawable.cell_normal);
                break;
            case SEMI_HIGHLIGHTED:
                setBackgroundResource(R.drawable.cell_semi_highlighted);
                break;
            case SELECTED:
                setBackgroundResource(R.drawable.cell_selected);
                break;
            case ERROR_SEMI_HIGHLIGHTED:
                setBackgroundResource(R.drawable.error_cell_semi_highlighted);
                break;
            case ERROR_SELECTED:
                setBackgroundResource(R.drawable.error_cell_selected);
                break;
        }
    }

    public int getRowIndex() {
        return mRowIndex;
    }

    void setRowIndex(int rowIndex) {
        mRowIndex = rowIndex;
    }

    public int getColIndex() {
        return mColIndex;
    }

    void setColIndex(int colIndex) {
        mColIndex = colIndex;
    }

    public boolean isPrefilled() {
        return mIsPrefilled;
    }

    public void setPrefilled(boolean prefilled) {
        mIsPrefilled = prefilled;
        if (prefilled) {
            setTextColor(getResources().getColor(R.color.cell_text_prefilled, null));
        } else {
            setTextColor(getResources().getColor(R.color.cell_text_user_fillable, null));
        }

    }

    public enum State {NORMAL, SEMI_HIGHLIGHTED, SELECTED, ERROR_SEMI_HIGHLIGHTED, ERROR_SELECTED}
}
