package ca.sfu.cmpt276.sudokulang.ui.game.board;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * State holder for SudokuBoard UI element.
 *
 * @implNote {@code updateNumEmptyCells()} must me called right before {@code getNumEmptyCells()} for it to work correctly.
 * @cite <a href="https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-4-saving-user-data/lesson-10-storing-data-with-room/10-1-c-room-livedata-viewmodel/10-1-c-room-livedata-viewmodel.html#viewmodel">LiveData & ViewModel</a>
 */
public class SudokuBoardViewModel extends ViewModel {
    private MutableLiveData<SudokuCellViewModel[][]> mCells;
    private MutableLiveData<Integer> mBoardSize, mSubgridHeight, mSubgridWidth;
    private MutableLiveData<Integer> mNumEmptyCells;
    private MutableLiveData<SudokuCellViewModel> mSelectedCell;

    /**
     * @implNote Board dimension when default constructed is undefined.
     */
    public SudokuBoardViewModel() {
        this(9, 3, 3);
    }

    public SudokuBoardViewModel(int boardSize, int subgridHeight, int subgridWidth) {
        super();
        init();
        createEmptyBoard(boardSize, subgridHeight, subgridWidth);
    }

    // NOTE: This initialization method must not be called again for the same object.
    private void init() {
        mBoardSize = new MutableLiveData<>();
        mSubgridHeight = new MutableLiveData<>();
        mSubgridWidth = new MutableLiveData<>();
        mSelectedCell = new MutableLiveData<>();
        mNumEmptyCells = new MutableLiveData<>();
        mCells = new MutableLiveData<>();
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

    public void generateBoardData() {
        // For TESTING only.
        // TODO: Replace this with a database. !!
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
        mCells.getValue()[0][3].setProperties(pair1.second, true, false);
        mCells.getValue()[2][6].setProperties(pair1.second, true, false);
        mCells.getValue()[4][7].setProperties(pair1.second, true, false);
        mCells.getValue()[6][5].setProperties(pair1.second, true, false);
        mCells.getValue()[7][1].setProperties(pair1.second, true, false);

        mCells.getValue()[0][2].setProperties(pair2.second, true, false);
        mCells.getValue()[1][4].setProperties(pair2.second, true, false);
        mCells.getValue()[3][1].setProperties(pair2.second, true, false);
        mCells.getValue()[6][7].setProperties(pair2.second, true, false);
        mCells.getValue()[7][0].setProperties(pair2.second, true, false);

        mCells.getValue()[1][1].setProperties(pair3.second, true, false);
        mCells.getValue()[4][2].setProperties(pair3.second, true, false);
        mCells.getValue()[5][8].setProperties(pair3.second, true, false);
        mCells.getValue()[7][3].setProperties(pair3.second, true, false);
        mCells.getValue()[8][6].setProperties(pair3.second, true, false);

        mCells.getValue()[1][6].setProperties(pair4.second, true, false);
        mCells.getValue()[2][4].setProperties(pair4.second, true, false);
        mCells.getValue()[4][1].setProperties(pair4.second, true, false);
        mCells.getValue()[5][7].setProperties(pair4.second, true, false);
        mCells.getValue()[6][2].setProperties(pair4.second, true, false);

        mCells.getValue()[0][6].setProperties(pair5.second, true, false);
        mCells.getValue()[2][0].setProperties(pair5.second, true, false);
        mCells.getValue()[3][7].setProperties(pair5.second, true, false);
        mCells.getValue()[4][5].setProperties(pair5.second, true, false);
        mCells.getValue()[5][1].setProperties(pair5.second, true, false);

        mCells.getValue()[3][2].setProperties(pair6.second, true, false);
        mCells.getValue()[4][4].setProperties(pair6.second, true, false);
        mCells.getValue()[5][6].setProperties(pair6.second, true, false);
        mCells.getValue()[7][5].setProperties(pair6.second, true, false);
        mCells.getValue()[8][7].setProperties(pair6.second, true, false);

        mCells.getValue()[0][1].setProperties(pair7.second, true, false);
        mCells.getValue()[2][5].setProperties(pair7.second, true, false);
        mCells.getValue()[3][3].setProperties(pair7.second, true, false);
        mCells.getValue()[5][2].setProperties(pair7.second, true, false);
        mCells.getValue()[6][4].setProperties(pair7.second, true, false);

        mCells.getValue()[2][1].setProperties(pair8.second, true, false);
        mCells.getValue()[7][4].setProperties(pair8.second, true, false);

        mCells.getValue()[1][8].setProperties(pair9.second, true, false);
        mCells.getValue()[3][6].setProperties(pair9.second, true, false);
        mCells.getValue()[6][3].setProperties(pair9.second, true, false);
        mCells.getValue()[7][7].setProperties(pair9.second, true, false);

        updateNumEmptyCells();
    }

    /**
     * Set the size of the Sudoku board and of its sub-grids.
     * Example: For a typical 9x9 board with 3x3 sub-grids, call: createEmptyBoard(9,3,3)
     *
     * @param boardSize     Number of cells in each column or row.
     * @param subgridHeight Number of cells in each sub-grid's column.
     *                      Equals {@code boardSize} if no sub-grid.
     * @param subgridWidth  Number of cells in each sub-grid's row.
     *                      Equals {@code boardSize} if no sub-grid.
     */
    public void createEmptyBoard(int boardSize, int subgridHeight, int subgridWidth) {
        assert (boardSize > 0);
        mBoardSize.setValue(boardSize);
        // Ensure sub-grids are equally divided.
        assert (subgridHeight > 0 && subgridHeight <= boardSize && boardSize % subgridHeight == 0);
        mSubgridHeight.setValue(subgridHeight);

        assert (subgridWidth > 0 && subgridWidth <= boardSize && boardSize % subgridWidth == 0);
        mSubgridWidth.setValue(subgridWidth);

        mSelectedCell.setValue(null);
        mNumEmptyCells.setValue(boardSize * boardSize);
        var cells = new SudokuCellViewModel[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                cells[i][j] = new SudokuCellViewModel();
                cells[i][j].setRowIndex(i);
                cells[i][j].setColIndex(j);
            }
        }
        mCells.setValue(cells);
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
            mSelectedCell.setValue(mCells.getValue()[rowIndex][colIndex]);
        }
    }

    public void setNoSelectedCell() {
        setSelectedCell(-1, -1);
    }

    public void updateNumEmptyCells() {
        int numEmptyCells = 0;
        for (var row : mCells.getValue()) {
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

    public LiveData<SudokuCellViewModel[][]> getCells() {
        return mCells;
    }

    public boolean isValidBoard() {
        for (var row : mCells.getValue()) {
            for (var cell : row) {
                if (cell.isErrorCell().getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidValueForCell(String value, @NonNull SudokuCellViewModel cell) {
        assert (value != null && !value.isBlank());
        // TODO
        return new Random().nextBoolean();
    }
}
