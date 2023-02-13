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

// NOTE: The layout parameters of this board are set in its XML file.
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
        setProperties(9, 3, 3);
    }

    /**
     * Set the size of the Sudoku board and of its sub-grids.
     * Example: For a typical 9x9 board with 3x3 sub-grids, do: setProperties(9,3,3)
     *
     * @param boardSize     Number of cells in each column or row.
     * @param subgridHeight Number of cells in each sub-grid's column.
     *                      Equals {@code boardSize} if no sub-grid.
     * @param subgridWidth  Number of cells in each sub-grid's row.
     *                      Equals {@code boardSize} if no sub-grid.
     */
    public void setProperties(int boardSize, int subgridHeight, int subgridWidth) {
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
        if (row.length == 1) {
            @IdRes int id = row[0].getId();
            constraintSet.centerHorizontally(id, ConstraintSet.PARENT_ID);
            constraintSet.centerVertically(id, ConstraintSet.PARENT_ID);
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
        if (column.length == 1) {
            @IdRes int id = column[0].getId();
            constraintSet.centerVertically(id, ConstraintSet.PARENT_ID);
            constraintSet.centerHorizontally(id, ConstraintSet.PARENT_ID);
            return;
        }
        @IdRes int[] colChainIds = Arrays.stream(column).mapToInt(View::getId).toArray();
        constraintSet.createVerticalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                colChainIds, null, chainStyle);
    }

    public String[][] getValues() {
        var values = new String[mBoardSize][mBoardSize];
        for (int i = 0; i < mBoardSize; i++) {
            for (int j = 0; j < mBoardSize; j++) {
                values[i][j] = (String) mCells[i][j].getText();
            }
        }
        return values;
    }

    public void setValues(@NonNull String[][] values) {
        for (int i = 0; i < mBoardSize; i++) {
            for (int j = 0; j < mBoardSize; j++) {
                final var value = values[i][j];
                assert (value != null);
                mCells[i][j].setText(value);
            }
        }
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

    public SudokuCell[][] getCells() {
        return mCells;
    }

    public @IdRes int[][] getCellsIds() {
        var ids = new int[mBoardSize][mBoardSize];
        for (int i = 0; i < mBoardSize; i++) {
            for (int j = 0; j < mBoardSize; j++) {
                ids[i][j] = mCells[i][j].getId();
            }
        }
        return ids;
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
