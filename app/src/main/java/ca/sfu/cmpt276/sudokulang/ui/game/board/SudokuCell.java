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
import ca.sfu.cmpt276.sudokulang.ui.Util;

/**
 * A UI representation of a Sudoku cell.
 *
 * @implNote Default state: <p>
 * Row index = -1.        <p>
 * Column index = -1.     <p>
 * Is user fillable.      <p>
 * Is not an error cell.
 */
@SuppressLint("AppCompatCustomView")
public class SudokuCell extends TextView {
    private int mRowIndex, mColIndex;
    private boolean mIsPrefilled, mIsErrorCell;

    public SudokuCell(@NonNull Context context) {
        super(context);
        init();
    }

    public SudokuCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
        final int padding = Util.dpToPx(2);
        setPadding(padding, padding, padding, padding);

        setGravity(Gravity.CENTER);
        setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelSmall);

        setText("");
        setColor(Color.NORMAL);
        setRowIndex(-1);
        setColIndex(-1);
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

    public String getText() {
        return (String) super.getText();
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

    public boolean isErrorCell() {
        return mIsErrorCell;
    }

    public void setAsErrorCell(boolean isErrorCell) {
        mIsErrorCell = isErrorCell;
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

    enum Color {NORMAL, SEMI_HIGHLIGHTED, SELECTED, ERROR_SEMI_HIGHLIGHTED, ERROR_SELECTED, ERROR_NOT_SELECTED}
}
