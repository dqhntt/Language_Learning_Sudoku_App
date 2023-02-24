package ca.sfu.cmpt276.sudokulang.ui.game.board;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Immutable state holder for SudokuBoard UI element.
 */
public final class BoardUiState {
    private final int mBoardSize, mSubgridHeight, mSubgridWidth;
    private final int mSelectedRowIndex, mSelectedColIndex;
    private final @NonNull CellUiState[][] mCells;

    /**
     * @implNote Board dimension when default constructed is undefined.
     */
    public BoardUiState() {
        this(1, 1, 1,
                -1, -1,
                new CellUiState[][]{new CellUiState[]{new CellUiState()}});
    }

    public BoardUiState(int boardSize, int subgridHeight, int subgridWidth,
                        int selectedRowIndex, int selectedColIndex,
                        @NonNull CellUiState[][] cells) {
        if (!isValidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
            throw new IllegalArgumentException("Invalid board dimension");
        }
        mBoardSize = boardSize;
        mSubgridHeight = subgridHeight;
        mSubgridWidth = subgridWidth;
        mSelectedRowIndex = selectedRowIndex;
        mSelectedColIndex = selectedColIndex;
        mCells = cells;
    }

    private boolean isValidBoardDimension(int boardSize, int subgridHeight, int subgridWidth) {
        // Ensure sub-grids are equally divided.
        return (subgridHeight > 0 && subgridHeight <= boardSize) && (boardSize % subgridHeight == 0)
                && (subgridWidth > 0 && subgridWidth <= boardSize) && (boardSize % subgridWidth == 0);
    }

    public int getBoardSize() {
        return mBoardSize;
    }

    public int getSubgridHeight() {
        return mSubgridHeight;
    }

    public int getSubgridWidth() {
        return mSubgridWidth;
    }

    public int getSelectedRowIndex() {
        return mSelectedRowIndex;
    }

    public int getSelectedColIndex() {
        return mSelectedColIndex;
    }

    /**
     * @return State of the selected cell or {@code null} if none.
     */
    public @Nullable CellUiState getSelectedCell() {
        return areValidIndexes(mSelectedRowIndex, mSelectedColIndex)
                ? mCells[mSelectedRowIndex][mSelectedColIndex]
                : null;
    }

    private boolean areValidIndexes(int rowIndex, int colIndex) {
        return (rowIndex >= 0 && rowIndex < mCells.length)
                && (colIndex >= 0 && colIndex < mCells[0].length);
    }

    public @NonNull CellUiState[][] getCells() {
        return mCells;
    }

    public boolean isSolvedBoard() {
        return (getNumEmptyCells() == 0) && hasNoErrorCells();
    }

    private int getNumEmptyCells() {
        int numEmptyCells = 0;
        for (var row : mCells) {
            for (var cell : row) {
                if (cell.getText().isBlank()) {
                    numEmptyCells++;
                }
            }
        }
        return numEmptyCells;
    }

    private boolean hasNoErrorCells() {
        for (var row : mCells) {
            for (var cell : row) {
                if (cell.isErrorCell()) {
                    return false;
                }
            }
        }
        return true;
    }
}
