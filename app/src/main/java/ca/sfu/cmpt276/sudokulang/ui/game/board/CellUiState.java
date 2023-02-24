package ca.sfu.cmpt276.sudokulang.ui.game.board;

import androidx.annotation.NonNull;

/**
 * Immutable state holder for SudokuCell UI element.
 */
public final class CellUiState {
    private final @NonNull String mText;
    private final boolean mIsPrefilled, mIsErrorCell;

    /**
     * @implNote Default state:        <p>
     * {@code getText() == ""}         <p>
     * {@code isPrefilled() == false}  <p>
     * {@code isErrorCell() == false}
     */
    public CellUiState() {
        this("", false, false);
    }

    public CellUiState(@NonNull String text, boolean isPrefilled, boolean isErrorCell) {
        if (isPrefilled && isErrorCell) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mText = text;
        mIsPrefilled = isPrefilled;
        mIsErrorCell = isErrorCell;
    }

    public @NonNull String getText() {
        return mText;
    }

    public boolean isPrefilled() {
        return mIsPrefilled;
    }

    public boolean isErrorCell() {
        return mIsErrorCell;
    }
}