package ca.sfu.cmpt276.sudokulang.ui.game.board;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * State holder and processor for SudokuBoard UI element.
 *
 * @implNote {@code updateNumEmptyCells()} must me called right before {@code getNumEmptyCells()} for it to work correctly.
 * @cite <a href="https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-4-saving-user-data/lesson-10-storing-data-with-room/10-1-c-room-livedata-viewmodel/10-1-c-room-livedata-viewmodel.html#viewmodel">LiveData & ViewModel</a>
 */
public class SudokuBoardViewModel extends ViewModel {
    private final MutableLiveData<Integer> mBoardSize, mSubgridHeight, mSubgridWidth;
    private final MutableLiveData<Integer> mNumEmptyCells;
    private final MutableLiveData<SudokuCellViewModel> mSelectedCell;
    private SudokuCellViewModel[][] mCells;

    /**
     * @implNote Board dimension when default constructed is undefined.
     */
    public SudokuBoardViewModel() {
        this(9, 3, 3);
    }

    public SudokuBoardViewModel(int boardSize, int subgridHeight, int subgridWidth) {
        super();
        mBoardSize = new MutableLiveData<>();
        mSubgridHeight = new MutableLiveData<>();
        mSubgridWidth = new MutableLiveData<>();
        mSelectedCell = new MutableLiveData<>();
        mNumEmptyCells = new MutableLiveData<>();
        mCells = null;
        generateNewBoard(boardSize, subgridHeight, subgridWidth);
    }

    /**
     * @param boardSize     Number of cells in each column or row.
     * @param subgridHeight Number of cells in each sub-grid's column.
     *                      Equals {@code boardSize} if no sub-grid.
     * @param subgridWidth  Number of cells in each sub-grid's row.
     *                      Equals {@code boardSize} if no sub-grid.
     */
    public void generateNewBoard(int boardSize, int subgridHeight, int subgridWidth) {
        createEmptyBoard(boardSize, subgridHeight, subgridWidth);
        generateBoardData();
    }

    private void createEmptyBoard(int boardSize, int subgridHeight, int subgridWidth) {
        if (!isValidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
            throw new IllegalArgumentException("Invalid board dimension");
        }
        mBoardSize.setValue(boardSize);
        mSubgridHeight.setValue(subgridHeight);
        mSubgridWidth.setValue(subgridWidth);
        mSelectedCell.setValue(null);
        mNumEmptyCells.setValue(boardSize * boardSize);
        mCells = new SudokuCellViewModel[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                mCells[i][j] = new SudokuCellViewModel();
                mCells[i][j].setRowIndex(i);
                mCells[i][j].setColIndex(j);
            }
        }
    }

    private boolean isValidBoardDimension(int boardSize, int subgridHeight, int subgridWidth) {
        // Ensure sub-grids are equally divided.
        return (subgridHeight > 0 && subgridHeight <= boardSize) && (boardSize % subgridHeight == 0)
                && (subgridWidth > 0 && subgridWidth <= boardSize) && (boardSize % subgridWidth == 0);
    }

    /**
     * @return LiveData of: the selected cell or {@code null} if none.
     */
    public LiveData<SudokuCellViewModel> getSelectedCell() {
        return mSelectedCell;
    }

    /**
     * Set a cell as selected.
     *
     * @param rowIndex Row index of the cell, -1 if none.
     * @param colIndex Column index of the cell, -1 if none.
     */
    public void setSelectedCell(int rowIndex, int colIndex) {
        assert (rowIndex >= -1 && rowIndex < mBoardSize.getValue());
        assert (colIndex >= -1 && colIndex < mBoardSize.getValue());
        if (rowIndex == -1 || colIndex == -1) {
            mSelectedCell.setValue(null);
        } else {
            mSelectedCell.setValue(mCells[rowIndex][colIndex]);
        }
    }

    public void setNoSelectedCell() {
        setSelectedCell(-1, -1);
    }

    public void updateNumEmptyCells() {
        int numEmptyCells = 0;
        for (var row : mCells) {
            for (var cell : row) {
                if (cell.getText().getValue().isBlank()) {
                    numEmptyCells++;
                }
            }
        }
        mNumEmptyCells.setValue(numEmptyCells);
    }

    public LiveData<Integer> getNumEmptyCells() {
        return mNumEmptyCells;
    }

    public LiveData<Integer> getBoardSize() {
        return mBoardSize;
    }

    public LiveData<Integer> getSubgridHeight() {
        return mSubgridHeight;
    }

    public LiveData<Integer> getSubgridWidth() {
        return mSubgridWidth;
    }

    public SudokuCellViewModel[][] getCells() {
        return mCells;
    }

    public boolean isValidBoard() {
        for (var row : mCells) {
            for (var cell : row) {
                if (cell.isErrorCell().getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidValueForCell(@NonNull String buttonValue, @NonNull SudokuCellViewModel cell) {
        return isValidValueForCell(buttonValue,
                cell.getRowIndex().getValue(),
                cell.getColIndex().getValue());
    }

    public boolean isValidValueForCell(@NonNull String buttonValue, int rowIndex, int colIndex) {
        if (mSubgridWidth.getValue().intValue() != mBoardSize.getValue().intValue()
                || mSubgridHeight.getValue().intValue() != mBoardSize.getValue().intValue()) {
            return isValidValueForCellInRowAndColumn(buttonValue, rowIndex, colIndex)
                    && isValidValueForCellInSubgrid(buttonValue, rowIndex, colIndex);
        } else {
            return isValidValueForCellInRowAndColumn(buttonValue, rowIndex, colIndex);
        }
    }

    private boolean isValidValueForCellInRowAndColumn(@NonNull String buttonValue, int rowIndex, int colIndex) {
        final var boardSize = mBoardSize.getValue();
        var buttonToCellValueMap = new HashMap<String, String>(boardSize);
        for (var pair : getDataValuePairs()) {
            buttonToCellValueMap.put(pair.first, pair.second);
        }
        for (int i = 0; i < boardSize; i++) {
            if (i == rowIndex) {
                continue;
            }
            final var cellValue = mCells[i][colIndex].getText().getValue();
            if (cellValue.equals(buttonValue)
                    || cellValue.equals(buttonToCellValueMap.get(buttonValue))) {
                return false;
            }
        }
        for (int j = 0; j < boardSize; j++) {
            if (j == colIndex) {
                continue;
            }
            final var cellValue = mCells[rowIndex][j].getText().getValue();
            if (cellValue.equals(buttonValue)
                    || cellValue.equals(buttonToCellValueMap.get(buttonValue))) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidValueForCellInSubgrid(@NonNull String buttonValue, int rowIndex, int colIndex) {
        final var boardSize = mBoardSize.getValue();
        var buttonToCellValueMap = new HashMap<String, String>(boardSize);
        for (var pair : getDataValuePairs()) {
            buttonToCellValueMap.put(pair.first, pair.second);
        }
        final int startRowIndex = rowIndex - rowIndex % mSubgridHeight.getValue();
        final int startColIndex = colIndex - colIndex % mSubgridWidth.getValue();
        final int endRowIndex = startRowIndex + mSubgridHeight.getValue() - 1;
        final int endColIndex = startColIndex + mSubgridWidth.getValue() - 1;
        for (int i = startRowIndex; i <= endRowIndex; i++) {
            for (int j = startColIndex; j <= endColIndex; j++) {
                if (i == rowIndex && j == colIndex) {
                    continue;
                }
                final var cellValue = mCells[i][j].getText().getValue();
                if (cellValue.equals(buttonValue)
                        || cellValue.equals(buttonToCellValueMap.get(buttonValue))) {
                    return false;
                }
            }
        }
        return true;
    }

    public Pair<String, String>[] getDataValuePairs() {
        // For TESTING only.
        // TODO: Replace this with a database. !!
        var pairs = new ArrayList<Pair<String, String>>(mBoardSize.getValue());
        pairs.add(new Pair<>("Ag", "Silver"));
        pairs.add(new Pair<>("Br", "Bromine"));
        pairs.add(new Pair<>("Ca", "Calcium"));
        pairs.add(new Pair<>("Fe", "Iron"));
        pairs.add(new Pair<>("He", "Helium"));
        pairs.add(new Pair<>("Kr", "Krypton"));
        pairs.add(new Pair<>("Li", "Lithium"));
        pairs.add(new Pair<>("Pb", "Lead"));
        pairs.add(new Pair<>("Se", "Selenium"));
        return pairs.toArray(new Pair[0]);
    }

    private void generateBoardData() {
        // For TESTING only.
        // TODO: Replace this with a database. !!
        if (mBoardSize.getValue() != 9 && mSubgridHeight.getValue() != 3 && mSubgridWidth.getValue() != 3) {
            return;
        }
        final var dataValuePairs = getDataValuePairs();
        final var pair1 = dataValuePairs[0];
        final var pair2 = dataValuePairs[1];
        final var pair3 = dataValuePairs[2];
        final var pair4 = dataValuePairs[3];
        final var pair5 = dataValuePairs[4];
        final var pair6 = dataValuePairs[5];
        final var pair7 = dataValuePairs[6];
        final var pair8 = dataValuePairs[7];
        final var pair9 = dataValuePairs[8];
        mCells[0][3].setProperties(pair1.second, true, false);
        mCells[2][6].setProperties(pair1.second, true, false);
        mCells[4][7].setProperties(pair1.second, true, false);
        mCells[6][5].setProperties(pair1.second, true, false);
        mCells[7][1].setProperties(pair1.second, true, false);

        mCells[0][2].setProperties(pair2.second, true, false);
        mCells[1][4].setProperties(pair2.second, true, false);
        mCells[3][1].setProperties(pair2.second, true, false);
        mCells[6][7].setProperties(pair2.second, true, false);
        mCells[7][0].setProperties(pair2.second, true, false);

        mCells[1][1].setProperties(pair3.second, true, false);
        mCells[4][2].setProperties(pair3.second, true, false);
        mCells[5][8].setProperties(pair3.second, true, false);
        mCells[7][3].setProperties(pair3.second, true, false);
        mCells[8][6].setProperties(pair3.second, true, false);

        mCells[1][6].setProperties(pair4.second, true, false);
        mCells[2][4].setProperties(pair4.second, true, false);
        mCells[4][1].setProperties(pair4.second, true, false);
        mCells[5][7].setProperties(pair4.second, true, false);
        mCells[6][2].setProperties(pair4.second, true, false);

        mCells[0][6].setProperties(pair5.second, true, false);
        mCells[2][0].setProperties(pair5.second, true, false);
        mCells[3][7].setProperties(pair5.second, true, false);
        mCells[4][5].setProperties(pair5.second, true, false);
        mCells[5][1].setProperties(pair5.second, true, false);

        mCells[3][2].setProperties(pair6.second, true, false);
        mCells[4][4].setProperties(pair6.second, true, false);
        mCells[5][6].setProperties(pair6.second, true, false);
        mCells[7][5].setProperties(pair6.second, true, false);
        mCells[8][7].setProperties(pair6.second, true, false);

        mCells[0][1].setProperties(pair7.second, true, false);
        mCells[2][5].setProperties(pair7.second, true, false);
        mCells[3][3].setProperties(pair7.second, true, false);
        mCells[5][2].setProperties(pair7.second, true, false);
        mCells[6][4].setProperties(pair7.second, true, false);

        mCells[2][1].setProperties(pair8.second, true, false);
        mCells[7][4].setProperties(pair8.second, true, false);

        mCells[1][8].setProperties(pair9.second, true, false);
        mCells[3][6].setProperties(pair9.second, true, false);
        mCells[6][3].setProperties(pair9.second, true, false);
        mCells[7][7].setProperties(pair9.second, true, false);
        // updateNumEmptyCells();
    }
}
