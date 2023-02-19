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
import ca.sfu.cmpt276.sudokulang.ui.Util;

/**
 * @implNote Board dimension when default constructed is undefined. <p/>
 * The layout parameters of this board are set in its XML file.
 */
public class SudokuBoard extends ConstraintLayout {

    private int mBoardSize, mSubgridHeight, mSubgridWidth;
    private SudokuCell[][] mCells;

    public SudokuBoard(@NonNull Context context) {
        super(context);
        init();
    }

    public SudokuBoard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        createEmptyBoard(1, 1, 1);
    }

    /**
     * Set the size of the Sudoku board and of its sub-grids.
     * Example: For a typical 9x9 board with 3x3 sub-grids, do: createEmptyBoard(9,3,3)
     *
     * @param boardSize     Number of cells in each column or row.
     * @param subgridHeight Number of cells in each sub-grid's column.
     *                      Equals {@code boardSize} if no sub-grid.
     * @param subgridWidth  Number of cells in each sub-grid's row.
     *                      Equals {@code boardSize} if no sub-grid.
     * @implNote This function creates new cells, thus clearing all OnClickListeners present.
     */
    public void createEmptyBoard(int boardSize, int subgridHeight, int subgridWidth) {
        assert (boardSize > 0);
        mBoardSize = boardSize;
        // Ensure sub-grids are equally divided.
        assert (subgridHeight > 0 && subgridHeight <= boardSize && boardSize % subgridHeight == 0);
        mSubgridHeight = subgridHeight;
        assert (subgridWidth > 0 && subgridWidth <= boardSize && boardSize % subgridWidth == 0);
        mSubgridWidth = subgridWidth;

        mCells = new SudokuCell[boardSize][boardSize];

        // Create and add cells to layout.
        // Cite: https://stackoverflow.com/a/40527407
        for (int n = 0; n < boardSize * boardSize; n++) {
            var cell = new SudokuCell(getContext());
            final int rowIndex = n / boardSize;
            final int colIndex = n % boardSize;
            cell.setRowIndex(rowIndex);
            cell.setColIndex(colIndex);
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
        chainRowsInLayout(mCells, constraintSet, ConstraintSet.CHAIN_SPREAD);
        chainColumnsInLayout(mCells, constraintSet, ConstraintSet.CHAIN_SPREAD);

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
        chainRowInLayout(vertDividers, constraintSet, ConstraintSet.CHAIN_SPREAD);
        chainColumnInLayout(horDividers, constraintSet, ConstraintSet.CHAIN_SPREAD);

        constraintSet.applyTo(this);
    }

    // See: https://constraintlayout.com/basics/create_chains.html
    private void chainRowsInLayout(@NonNull View[][] matrix, ConstraintSet constraintSet, int chainStyle) {
        for (var row : matrix) {
            chainRowInLayout(row, constraintSet, chainStyle);
        }
    }

    // Cite: https://stackoverflow.com/a/23945015
    private void chainRowInLayout(@NonNull View[] row, ConstraintSet constraintSet, int chainStyle) {
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
                rowChainIds, null, chainStyle);
    }

    private void chainColumnsInLayout(@NonNull View[][] matrix, ConstraintSet constraintSet, int chainStyle) {
        for (var column : transpose(matrix)) {
            chainColumnInLayout(column, constraintSet, chainStyle);
        }
    }

    private void chainColumnInLayout(@NonNull View[] column, ConstraintSet constraintSet, int chainStyle) {
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
                colChainIds, null, chainStyle);
    }

    public SudokuCell[][] getCells() {
        return mCells;
    }

    public void highlightRelatedCells(@Nullable SudokuCellViewModel cell) {
        if (cell == null) {
            highlightRelatedCells(-1, -1);
        } else {
            highlightRelatedCells(cell.getRowIndex().getValue(), cell.getColIndex().getValue());
        }
    }

    /**
     * Highlight the column, row, and sub-grid related to a cell and only that cell.
     *
     * @param rowIndex Row index of that cell, -1 if none.
     * @param colIndex Column index of that cell, -1 if none.
     */
    private void highlightRelatedCells(int rowIndex, int colIndex) {
        final boolean validIndexes = (rowIndex >= 0 && rowIndex < mBoardSize)
                && (colIndex >= 0 && colIndex < mBoardSize);
        resetBoardColors();
        if (!validIndexes) {
            return;
        }
        final boolean isErrorCell = mCells[rowIndex][colIndex].isErrorCell();
        highlightRow(rowIndex, isErrorCell);
        highlightColumn(colIndex, isErrorCell);
        if (mSubgridWidth != mBoardSize || mSubgridHeight != mBoardSize) {
            highlightSubgrid(rowIndex, colIndex);
        }
        final var cellColor = isErrorCell
                ? SudokuCell.Color.ERROR_SELECTED
                : SudokuCell.Color.SELECTED;
        mCells[rowIndex][colIndex].setColor(cellColor);
    }

    private void highlightColumn(int colIndex, boolean isErrorCell) {
        for (int i = 0; i < mBoardSize; i++) {
            var currentCell = mCells[i][colIndex];
            if (currentCell.isErrorCell()) {
                currentCell.setColor(SudokuCell.Color.ERROR_NOT_SELECTED);
            } else {
                currentCell.setColor(isErrorCell
                        ? SudokuCell.Color.ERROR_SEMI_HIGHLIGHTED
                        : SudokuCell.Color.SEMI_HIGHLIGHTED);
            }
        }
    }

    private void highlightRow(int rowIndex, boolean isErrorCell) {
        for (int j = 0; j < mBoardSize; j++) {
            var currentCell = mCells[rowIndex][j];
            if (currentCell.isErrorCell()) {
                currentCell.setColor(SudokuCell.Color.ERROR_NOT_SELECTED);
            } else {
                currentCell.setColor(isErrorCell
                        ? SudokuCell.Color.ERROR_SEMI_HIGHLIGHTED
                        : SudokuCell.Color.SEMI_HIGHLIGHTED);
            }
        }
    }

    private void highlightSubgrid(int rowIndex, int colIndex) {
        final int startRowIndex = rowIndex - rowIndex % mSubgridHeight;
        final int startColIndex = colIndex - colIndex % mSubgridWidth;
        final int endRowIndex = startRowIndex + mSubgridHeight - 1;
        final int endColIndex = startColIndex + mSubgridWidth - 1;
        for (int i = startRowIndex; i <= endRowIndex; i++) {
            for (int j = startColIndex; j <= endColIndex; j++) {
                var currentCell = mCells[i][j];
                if (currentCell.isErrorCell()) {
                    currentCell.setColor(SudokuCell.Color.ERROR_NOT_SELECTED);
                } else {
                    currentCell.setColor(mCells[rowIndex][colIndex].isErrorCell()
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
        for (var row : mCells) {
            for (var cell : row) {
                cell.setColor(cell.isErrorCell()
                        ? SudokuCell.Color.ERROR_NOT_SELECTED
                        : SudokuCell.Color.NORMAL);
            }
        }
    }

    private @NonNull View[][] transpose(@NonNull View[][] matrix) {
        final int nRows = matrix.length;
        final int nCols = matrix[0].length;
        var transposedMatrix = new View[nCols][nRows];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return transposedMatrix;
    }

    private static class Divider extends View {
        protected Divider(@NonNull Context context) {
            super(context);
            init();
        }

        private void init() {
            setId(generateViewId());
            setBackgroundResource(R.color.grid_divider);
        }

        protected void setOrientation(boolean horizontal) {
            final int thickness = Util.dpToPx(1);
            var layoutParams = horizontal
                    ? new ConstraintLayout.LayoutParams(0, thickness)
                    : new ConstraintLayout.LayoutParams(thickness, 0);
            setLayoutParams(layoutParams);
        }
    }

    private static class HorizontalDivider extends Divider {
        public HorizontalDivider(@NonNull Context context) {
            super(context);
            setOrientation(true);
        }
    }

    private static class VerticalDivider extends Divider {
        public VerticalDivider(@NonNull Context context) {
            super(context);
            setOrientation(false);
        }
    }
}
