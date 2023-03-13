package ca.sfu.cmpt276.sudokulang.ui.game.board;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Arrays;

import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.ui.UiUtil;

/**
 * A UI representation of a Sudoku board.
 *
 * @implNote Board dimension when default constructed is undefined. <p>
 * The layout parameters of this board are set in its XML file.
 */
public class SudokuBoard extends ConstraintLayout {
    private @NonNull BoardUiState mUiState;
    private SudokuCell[][] mCells;

    public SudokuBoard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mUiState = new BoardUiState();
        createBoard(mUiState);
    }

    /**
     * Set the size of the Sudoku board and of its sub-grids.
     * Example: For a typical 9x9 board with 3x3 sub-grids, do: {@code createEmptyBoard(9,3,3)}
     *
     * @param boardSize     Number of cells in each column or row.
     * @param subgridHeight Number of cells in each sub-grid's column.
     *                      Equals {@code boardSize} if no sub-grid.
     * @param subgridWidth  Number of cells in each sub-grid's row.
     *                      Equals {@code boardSize} if no sub-grid.
     * @implNote This function creates new cells, thus clearing all {@link View#OnClickListener} present.
     */
    private void createEmptyBoard(int boardSize, int subgridHeight, int subgridWidth) {
        // Ensure sub-grids are equally divided.
        assert (subgridHeight > 0 && subgridHeight <= boardSize && boardSize % subgridHeight == 0);
        assert (subgridWidth > 0 && subgridWidth <= boardSize && boardSize % subgridWidth == 0);

        mCells = new SudokuCell[boardSize][boardSize];

        // Create and add cells to layout.
        // Cite: https://stackoverflow.com/a/40527407
        for (int n = 0; n < boardSize * boardSize; n++) {
            final int rowIndex = n / boardSize;
            final int colIndex = n % boardSize;
            final var cell = new SudokuCell(getContext(), rowIndex, colIndex);
            mCells[rowIndex][colIndex] = cell;
            addView(cell);
        }

        // Add dividers to layout.
        final int nVertDividers = boardSize / subgridWidth - 1;
        var vertDividers = new VerticalDivider[nVertDividers];
        for (int i = 0; i < nVertDividers; i++) {
            var vD = new VerticalDivider(getContext());
            vertDividers[i] = vD;
            addView(vD);
        }
        final int nHorDividers = boardSize / subgridHeight - 1;
        var horDividers = new HorizontalDivider[nHorDividers];
        for (int i = 0; i < nHorDividers; i++) {
            var hD = new HorizontalDivider(getContext());
            horDividers[i] = hD;
            addView(hD);
        }

        // Start constraining cells in this layout.
        // Cite: https://pratapsharma.com.np/adding-views-to-constraint-layout-programmatically
        // See: https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintSet
        var constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        // Chain cells to each other.
        chainRowsInLayout(mCells, constraintSet);
        chainColumnsInLayout(mCells, constraintSet);

        // Constrain cells to dividers
        // and dividers to cells, temporarily.
        for (int i = 0; i < boardSize; i++) {
            // To vertical dividers.
            for (int j = 1; j <= nVertDividers; j++) {
                constraintSet.addToHorizontalChain(
                        vertDividers[j - 1].getId(),
                        mCells[i][subgridWidth * j - 1].getId(),
                        mCells[i][subgridWidth * j].getId());
            }
            // To horizontal dividers.
            for (int k = 1; k <= nHorDividers; k++) {
                constraintSet.addToVerticalChain(
                        horDividers[k - 1].getId(),
                        mCells[subgridHeight * k - 1][i].getId(),
                        mCells[subgridHeight * k][i].getId());
            }
        }

        // Chain dividers to each other.
        chainRowInLayout(vertDividers, constraintSet);
        chainColumnInLayout(horDividers, constraintSet);

        constraintSet.applyTo(this);
    }

    public void createBoard(@NonNull BoardUiState uiState) {
        createEmptyBoard(
                uiState.getBoardSize(),
                uiState.getSubgridHeight(),
                uiState.getSubgridWidth()
        );
        updateState(uiState);
    }

    // See: https://constraintlayout.com/basics/create_chains.html
    private void chainRowsInLayout(@NonNull View[][] matrix, ConstraintSet constraintSet) {
        for (var row : matrix) {
            chainRowInLayout(row, constraintSet);
        }
    }

    // Cite: https://stackoverflow.com/a/23945015
    private void chainRowInLayout(@NonNull View[] row, ConstraintSet constraintSet) {
        if (row.length < 1) {
            return;
        }
        if (row.length == 1) {
            constraintSet.centerHorizontally(row[0].getId(), ConstraintSet.PARENT_ID);
            return;
        }
        @IdRes int[] rowChainIds = Arrays.stream(row).mapToInt(View::getId).toArray();
        constraintSet.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                rowChainIds, null, ConstraintSet.CHAIN_SPREAD);
    }

    private void chainColumnsInLayout(@NonNull View[][] matrix, ConstraintSet constraintSet) {
        for (var column : UiUtil.transpose(matrix)) {
            chainColumnInLayout(column, constraintSet);
        }
    }

    private void chainColumnInLayout(@NonNull View[] column, ConstraintSet constraintSet) {
        if (column.length < 1) {
            return;
        }
        if (column.length == 1) {
            constraintSet.centerVertically(column[0].getId(), ConstraintSet.PARENT_ID);
            return;
        }
        @IdRes int[] colChainIds = Arrays.stream(column).mapToInt(View::getId).toArray();
        constraintSet.createVerticalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                colChainIds, null, ConstraintSet.CHAIN_SPREAD);
    }

    /**
     * Highlight the column, row, and sub-grid related to a cell and only that cell.
     *
     * @param rowIndex Row index of that cell, -1 if none.
     * @param colIndex Column index of that cell, -1 if none.
     */
    private void highlightRelatedCells(int rowIndex, int colIndex) {
        resetBoardColors();
        final boolean validIndexes =
                (rowIndex >= 0 && rowIndex < mUiState.getBoardSize())
                        && (colIndex >= 0 && colIndex < mUiState.getBoardSize());
        if (!validIndexes) {
            return;
        }
        final boolean isErrorCell = mUiState.getCells()[rowIndex][colIndex].isErrorCell();
        highlightRow(rowIndex, isErrorCell);
        highlightColumn(colIndex, isErrorCell);
        if (mUiState.getSubgridHeight() != mUiState.getBoardSize()
                || mUiState.getSubgridWidth() != mUiState.getBoardSize()) {
            highlightSubgrid(rowIndex, colIndex);
        }
        final var cellColor = isErrorCell
                ? SudokuCell.Color.ERROR_SELECTED
                : SudokuCell.Color.SELECTED;
        mCells[rowIndex][colIndex].setColor(cellColor);
    }

    private void highlightColumn(int colIndex, boolean isErrorCell) {
        for (int i = 0; i < mUiState.getBoardSize(); i++) {
            final var currentCell = mCells[i][colIndex];
            final var currCellState = mUiState.getCells()[i][colIndex];
            if (currCellState.isErrorCell()) {
                currentCell.setColor(SudokuCell.Color.ERROR_NOT_SELECTED);
            } else {
                currentCell.setColor(isErrorCell
                        ? SudokuCell.Color.ERROR_SEMI_HIGHLIGHTED
                        : SudokuCell.Color.SEMI_HIGHLIGHTED);
            }
        }
    }

    private void highlightRow(int rowIndex, boolean isErrorCell) {
        for (int j = 0; j < mUiState.getBoardSize(); j++) {
            final var currentCell = mCells[rowIndex][j];
            final var currCellState = mUiState.getCells()[rowIndex][j];
            if (currCellState.isErrorCell()) {
                currentCell.setColor(SudokuCell.Color.ERROR_NOT_SELECTED);
            } else {
                currentCell.setColor(isErrorCell
                        ? SudokuCell.Color.ERROR_SEMI_HIGHLIGHTED
                        : SudokuCell.Color.SEMI_HIGHLIGHTED);
            }
        }
    }

    private void highlightSubgrid(int rowIndex, int colIndex) {
        final int startRowIndex = rowIndex - rowIndex % mUiState.getSubgridHeight();
        final int startColIndex = colIndex - colIndex % mUiState.getSubgridWidth();
        final int endRowIndex = startRowIndex + mUiState.getSubgridHeight() - 1;
        final int endColIndex = startColIndex + mUiState.getSubgridWidth() - 1;
        for (int i = startRowIndex; i <= endRowIndex; i++) {
            for (int j = startColIndex; j <= endColIndex; j++) {
                final var currentCell = mCells[i][j];
                final var currCellState = mUiState.getCells()[i][j];
                if (currCellState.isErrorCell()) {
                    currentCell.setColor(SudokuCell.Color.ERROR_NOT_SELECTED);
                } else {
                    currentCell.setColor(mUiState.getCells()[rowIndex][colIndex].isErrorCell()
                            ? SudokuCell.Color.ERROR_SEMI_HIGHLIGHTED
                            : SudokuCell.Color.SEMI_HIGHLIGHTED);
                }
            }
        }
    }

    /**
     * Register a callback to be invoked when each cell is clicked.
     *
     * @param l The callback that will run.
     */
    public void setOnclickListenersForAllCells(OnClickListener l) {
        for (var row : mCells) {
            for (var cell : row) {
                cell.setOnClickListener(l);
            }
        }
    }

    private void resetBoardColors() {
        final var boardSize = mUiState.getBoardSize();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                mCells[i][j].setColor(
                        mUiState.getCells()[i][j].isErrorCell()
                                ? SudokuCell.Color.ERROR_NOT_SELECTED
                                : SudokuCell.Color.NORMAL
                );
            }
        }
    }

    public void updateState(@NonNull BoardUiState uiState) {
        final var boardSize = uiState.getBoardSize();
        final var subgridHeight = uiState.getSubgridHeight();
        final var subgridWidth = uiState.getSubgridWidth();
        if (boardSize != mUiState.getBoardSize()
                || subgridHeight != mUiState.getSubgridHeight()
                || subgridWidth != mUiState.getSubgridWidth()) {
            createEmptyBoard(boardSize, subgridHeight, subgridWidth);
        }
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                mCells[i][j].updateState(uiState.getCells()[i][j]);
            }
        }
        mUiState = uiState;
        highlightRelatedCells(uiState.getSelectedRowIndex(), uiState.getSelectedColIndex());
    }

    private static class Divider extends View {
        final static int mThickness = UiUtil.dpToPx(1);
        protected Divider(@NonNull Context context) {
            super(context);
            setId(generateViewId());
            setBackgroundResource(R.color.grid_divider);
        }
        protected void setHorizontalOrientation(boolean isHorizontal) {
            var layoutParams = isHorizontal
                    ? new ConstraintLayout.LayoutParams(0, mThickness)
                    : new ConstraintLayout.LayoutParams(mThickness, 0);
            setLayoutParams(layoutParams);
        }
    }

    private static class HorizontalDivider extends Divider {
        public HorizontalDivider(@NonNull Context context) {
            super(context);
            setHorizontalOrientation(true);
        }
    }

    private static class VerticalDivider extends Divider {
        public VerticalDivider(@NonNull Context context) {
            super(context);
            setHorizontalOrientation(false);
        }
    }
}
