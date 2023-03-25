package ca.sfu.cmpt276.sudokulang.ui.game.board;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.data.Cell;
import ca.sfu.cmpt276.sudokulang.data.CellImpl;
import ca.sfu.cmpt276.sudokulang.ui.UiUtil;

/**
 * A UI representation of a Sudoku cell.
 */
public class CellUi extends com.google.android.material.textview.MaterialTextView {
    private final static int mPadding = UiUtil.dpToPx(2);
    private final int mRowIndex, mColIndex;
    private @NonNull Cell mUiState;

    public CellUi(@NonNull Context context) {
        this(context, -1, -1);
    }

    public CellUi(@NonNull Context context, int rowIndex, int colIndex) {
        super(context);
        mRowIndex = rowIndex;
        mColIndex = colIndex;
        mUiState = new CellImpl();
        init();
    }

    private void init() {
        // Make it fill all available layout space, considering other weights.
        setLayoutParams(
                new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                        ConstraintLayout.LayoutParams.MATCH_CONSTRAINT)
        );

        // Make text stay 2dp away from borders.
        setPadding(mPadding, mPadding, mPadding, mPadding);

        // Center text inside cell.
        setGravity(Gravity.CENTER);
        setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelSmall);

        // Set text size (sp) == 28% view's height (dp).
        addOnLayoutChangeListener((view, left, top, right, bottom,
                                   oldLeft, oldTop, oldRight, oldBottom) ->
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 0.28f * UiUtil.pxToDp(bottom - top)));

        setText(mUiState.getText());
        setColor(Color.NORMAL);
        setId(generateViewId());
    }

    // Cite: https://stackoverflow.com/a/29445079
    void setColor(@NonNull Color color) {
        switch (color) {
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
            case ERROR_NOT_SELECTED:
                setBackgroundResource(R.drawable.error_cell_not_selected);
                break;
            default:
                throw new IllegalArgumentException("Unknown cell color");
        }
    }

    void updateState(@NonNull Cell uiState) {
        mUiState = uiState;
        setText(uiState.getText());
        if (uiState.isPrefilled()) {
            setTextColor(getResources().getColor(R.color.cell_text_prefilled, null));
        } else {
            setTextColor(getResources().getColor(R.color.cell_text_user_fillable, null));
        }
    }

    public int getRowIndex() {
        return mRowIndex;
    }

    public int getColIndex() {
        return mColIndex;
    }

    enum Color {NORMAL, SEMI_HIGHLIGHTED, SELECTED, ERROR_SEMI_HIGHLIGHTED, ERROR_SELECTED, ERROR_NOT_SELECTED}
}