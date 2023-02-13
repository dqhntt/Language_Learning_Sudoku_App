package ca.sfu.cmpt276.sudokulang.ui.game.board;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Arrays;

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
     * @param subgridWidth  Number of cells in each sub-grid's row.
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

        // TODO: Add dividers for sub-grids.

        // Add cells to layout.
        // Cite: https://stackoverflow.com/a/40527407
        for (int n = 0; n < boardSize * boardSize; n++) {
            var cell = new SudokuCell(getContext());
            final int rowIndex = n / boardSize;
            final int colIndex = n % boardSize;
            cell.setRowIndex(rowIndex);
            cell.setColIndex(colIndex);
            mCells[rowIndex][colIndex] = cell;
            addView(cell, n);
        }

        // Parameters to constrain cells in this layout.
        // Cite: https://pratapsharma.com.np/adding-views-to-constraint-layout-programmatically
        // See: https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintSet
        var constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        final @IdRes int parentId = getId();

        // Chain rows.
        // Cite: https://stackoverflow.com/a/23945015
        for (var cellRow : mCells) {
            int[] rowChainIds = Arrays.stream(cellRow).mapToInt(SudokuCell::getId).toArray();
            constraintSet.createHorizontalChain(
                    parentId, ConstraintSet.LEFT,
                    parentId, ConstraintSet.RIGHT,
                    rowChainIds, null, ConstraintSet.CHAIN_PACKED);
        }

        // Chain columns.
        for (var cellCol : transpose(mCells)) {
            int[] colChainIds = Arrays.stream(cellCol).mapToInt(SudokuCell::getId).toArray();
            constraintSet.createVerticalChain(
                    parentId, ConstraintSet.TOP,
                    parentId, ConstraintSet.BOTTOM,
                    colChainIds, null, ConstraintSet.CHAIN_PACKED);
        }

        constraintSet.applyTo(this);
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

    public SudokuCell[][] getCells() {
        return mCells;
    }

    private SudokuCell[][] transpose(@NonNull SudokuCell[][] matrix) {
        final int nRows = matrix.length;
        final int nCols = matrix[0].length;
        var transposedMatrix = new SudokuCell[nCols][nRows];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return transposedMatrix;
    }
}
