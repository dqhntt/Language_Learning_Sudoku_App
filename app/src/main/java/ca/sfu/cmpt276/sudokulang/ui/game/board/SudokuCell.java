package ca.sfu.cmpt276.sudokulang.ui.game.board;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.ui.UiUtil;

/**
 * A UI representation of a Sudoku cell.
 */
@SuppressLint("AppCompatCustomView")
public class SudokuCell extends TextView {
    private @NonNull CellUiState mUiState;
    private final int mRowIndex, mColIndex;

    public SudokuCell(@NonNull Context context, int rowIndex, int colIndex) {
        super(context);
        mRowIndex = rowIndex;
        mColIndex = colIndex;
        mUiState = new CellUiState();
        init();
    }

    public SudokuCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRowIndex = mColIndex = -1;
        mUiState = new CellUiState();
        init();
    }

    private void init() {
        // Make it fill all available layout space, considering other weights.
        var layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        layoutParams.horizontalWeight = layoutParams.verticalWeight = 1f;
        setLayoutParams(layoutParams);

        // Make text stay 2dp away from borders.
        final int padding = UiUtil.dpToPx(2);
        setPadding(padding, padding, padding, padding);

        // Center text inside cell.
        setGravity(Gravity.CENTER);
        setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelSmall);

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

    void updateState(@NonNull CellUiState uiState) {
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
