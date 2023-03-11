package ca.sfu.cmpt276.sudokulang.ui.game;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import ca.sfu.cmpt276.sudokulang.ui.game.board.BoardUiState;
import ca.sfu.cmpt276.sudokulang.ui.game.board.CellUiState;

/**
 * State processor for UI elements in GameFragment.
 *
 * @cite <a href="https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-4-saving-user-data/lesson-10-storing-data-with-room/10-1-c-room-livedata-viewmodel/10-1-c-room-livedata-viewmodel.html#viewmodel">LiveData & ViewModel</a>
 */
public class GameViewModel extends ViewModel {
    private final @NonNull MutableLiveData<BoardUiState> mBoardUiState;

    /**
     * @implNote Board dimension when default constructed is undefined. <p>
     * Use {@code generateNewBoard()} to set a desired dimension.
     */
    public GameViewModel() {
        mBoardUiState = new MutableLiveData<>();
    }

    public LiveData<BoardUiState> getBoardUiState() {
        return mBoardUiState;
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

    /**
     * Set a cell as selected.
     *
     * @param rowIndex Row index of the cell, -1 if none.
     * @param colIndex Column index of the cell, -1 if none.
     */
    public void setSelectedCell(int rowIndex, int colIndex) {
        mBoardUiState.setValue(new BoardUiState(
                mBoardUiState.getValue().getBoardSize(),
                mBoardUiState.getValue().getSubgridHeight(),
                mBoardUiState.getValue().getSubgridWidth(),
                rowIndex, colIndex,
                mBoardUiState.getValue().getCells()
        ));
    }

    public void setNoSelectedCell() {
        setSelectedCell(-1, -1);
    }

    public void setSelectedCellText(@NonNull String buttonValue) {
        setSelectedCellState(new CellUiState(
                buttonValue,
                false,
                !isValidValueForCell(
                        buttonValue,
                        mBoardUiState.getValue().getSelectedRowIndex(),
                        mBoardUiState.getValue().getSelectedColIndex()
                )
        ));
    }

    public void clearSelectedCell() {
        setSelectedCellState(new CellUiState());
    }

    private void setSelectedCellState(@NonNull CellUiState newState) {
        final var selectedCell = mBoardUiState.getValue().getSelectedCell();
        if (selectedCell != null && !selectedCell.isPrefilled()) {
            final var selectedRowIndex = mBoardUiState.getValue().getSelectedRowIndex();
            final var selectedColIndex = mBoardUiState.getValue().getSelectedColIndex();
            final var cells = mBoardUiState.getValue().getCells();
            cells[selectedRowIndex][selectedColIndex] = newState;
            mBoardUiState.setValue(new BoardUiState(
                    mBoardUiState.getValue().getBoardSize(),
                    mBoardUiState.getValue().getSubgridHeight(),
                    mBoardUiState.getValue().getSubgridWidth(),
                    selectedRowIndex, selectedColIndex,
                    cells
            ));
        }
    }

    private void createEmptyBoard(int boardSize, int subgridHeight, int subgridWidth) {
        if (!isValidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
            throw new IllegalArgumentException("Invalid board dimension");
        }
        final var newCells = new CellUiState[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                newCells[i][j] = new CellUiState();
            }
        }
        mBoardUiState.setValue(new BoardUiState(
                boardSize, subgridHeight, subgridWidth,
                -1, -1,
                newCells
        ));
    }

    private boolean isValidBoardDimension(int boardSize, int subgridHeight, int subgridWidth) {
        // Ensure sub-grids are equally divided.
        return (subgridHeight > 0 && subgridHeight <= boardSize) && (boardSize % subgridHeight == 0)
                && (subgridWidth > 0 && subgridWidth <= boardSize) && (boardSize % subgridWidth == 0);
    }

    private boolean isValidValueForCell(@NonNull String buttonValue, int rowIndex, int colIndex) {
        final int boardSize = mBoardUiState.getValue().getBoardSize();
        final int subgridHeight = mBoardUiState.getValue().getSubgridHeight();
        final int subgridWidth = mBoardUiState.getValue().getSubgridWidth();
        if (!areValidIndexes(rowIndex, colIndex)) {
            return false;
        }
        if (subgridWidth == boardSize && subgridHeight == boardSize) {
            return isValidValueForCellInRowAndColumn(buttonValue, rowIndex, colIndex);
        } else {
            return isValidValueForCellInRowAndColumn(buttonValue, rowIndex, colIndex)
                    && isValidValueForCellInSubgrid(buttonValue, rowIndex, colIndex);
        }
    }

    private boolean areValidIndexes(int rowIndex, int colIndex) {
        return (rowIndex >= 0 && rowIndex < mBoardUiState.getValue().getBoardSize())
                && (colIndex >= 0 && colIndex < mBoardUiState.getValue().getBoardSize());
    }

    private boolean isValidValueForCellInRowAndColumn(@NonNull String buttonValue, int rowIndex, int colIndex) {
        final int boardSize = mBoardUiState.getValue().getBoardSize();
        final var cells = mBoardUiState.getValue().getCells();
        var buttonToCellValueMap = getButtonToCellValueMap();
        for (int i = 0; i < boardSize; i++) {
            if (i == rowIndex) {
                continue;
            }
            final var cellValue = cells[i][colIndex].getText();
            if (cellValue.equals(buttonValue)
                    || cellValue.equals(buttonToCellValueMap.get(buttonValue))) {
                return false;
            }
        }
        for (int j = 0; j < boardSize; j++) {
            if (j == colIndex) {
                continue;
            }
            final var cellValue = cells[rowIndex][j].getText();
            if (cellValue.equals(buttonValue)
                    || cellValue.equals(buttonToCellValueMap.get(buttonValue))) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidValueForCellInSubgrid(@NonNull String buttonValue, int rowIndex, int colIndex) {
        final int subgridHeight = mBoardUiState.getValue().getSubgridHeight();
        final int subgridWidth = mBoardUiState.getValue().getSubgridWidth();
        var buttonToCellValueMap = getButtonToCellValueMap();
        final int startRowIndex = rowIndex - rowIndex % subgridHeight;
        final int startColIndex = colIndex - colIndex % subgridWidth;
        final int endRowIndex = startRowIndex + subgridHeight - 1;
        final int endColIndex = startColIndex + subgridWidth - 1;
        for (int i = startRowIndex; i <= endRowIndex; i++) {
            for (int j = startColIndex; j <= endColIndex; j++) {
                if (i == rowIndex && j == colIndex) {
                    continue;
                }
                final var cellValue = mBoardUiState.getValue().getCells()[i][j].getText();
                if (cellValue.equals(buttonValue)
                        || cellValue.equals(buttonToCellValueMap.get(buttonValue))) {
                    return false;
                }
            }
        }
        return true;
    }

    private HashMap<String, String> getButtonToCellValueMap() {
        var map = new HashMap<String, String>(mBoardUiState.getValue().getBoardSize());
        for (var pair : getDataValuePairs()) {
            map.put(pair.first, pair.second);
        }
        return map;
    }

    public Pair<String, String>[] getDataValuePairs() {
        // For TESTING only.
        // TODO: Replace this with a database. !!

        final int boardSize = mBoardUiState.getValue().getBoardSize();
        var pairs = new ArrayList<Pair<String, String>>(boardSize);
        if (boardSize > 0) {
            pairs.add(new Pair<>("Ag", "Silver"));
            pairs.add(new Pair<>("Cr", "Chromium"));
            pairs.add(new Pair<>("Fe", "Iron"));
            pairs.add(new Pair<>("He", "Helium"));
        }
        if (boardSize > 4) {
            pairs.add(new Pair<>("I", "Iodine"));
            pairs.add(new Pair<>("K", "Potassium"));
        }
        if (boardSize > 6) {
            pairs.add(new Pair<>("Li", "Lithium"));
            pairs.add(new Pair<>("Na", "Sodium"));
            pairs.add(new Pair<>("Pb", "Lead"));
        }
        if (boardSize > 9) {
            pairs.add(new Pair<>("Sn", "Tin"));
            pairs.add(new Pair<>("W", "Tungsten"));
            pairs.add(new Pair<>("Zn", "Zinc"));
        }
        return pairs.toArray(new Pair[0]);
    }

    private void generateBoardData() {
        // For TESTING only.
        // TODO: Replace this with a database. !!

        final int boardSize = mBoardUiState.getValue().getBoardSize();
        final int subgridHeight = mBoardUiState.getValue().getSubgridHeight();
        final int subgridWidth = mBoardUiState.getValue().getSubgridWidth();

        final var pairs = getDataValuePairs();
        final var cells = mBoardUiState.getValue().getCells();

        // 9x9
        if (boardSize == 9 && subgridHeight == 3 && subgridWidth == 3) {
            cells[0][3] = new CellUiState(pairs[0].second, true, false);
            cells[2][6] = new CellUiState(pairs[0].second, true, false);
            cells[4][7] = new CellUiState(pairs[0].second, true, false);
            cells[6][5] = new CellUiState(pairs[0].second, true, false);
            cells[7][1] = new CellUiState(pairs[0].second, true, false);

            cells[0][2] = new CellUiState(pairs[1].second, true, false);
            cells[1][4] = new CellUiState(pairs[1].second, true, false);
            cells[3][1] = new CellUiState(pairs[1].second, true, false);
            cells[6][7] = new CellUiState(pairs[1].second, true, false);
            cells[7][0] = new CellUiState(pairs[1].second, true, false);

            cells[1][1] = new CellUiState(pairs[2].second, true, false);
            cells[4][2] = new CellUiState(pairs[2].second, true, false);
            cells[5][8] = new CellUiState(pairs[2].second, true, false);
            cells[7][3] = new CellUiState(pairs[2].second, true, false);
            cells[8][6] = new CellUiState(pairs[2].second, true, false);

            cells[1][6] = new CellUiState(pairs[3].second, true, false);
            cells[2][4] = new CellUiState(pairs[3].second, true, false);
            cells[4][1] = new CellUiState(pairs[3].second, true, false);
            cells[5][7] = new CellUiState(pairs[3].second, true, false);
            cells[6][2] = new CellUiState(pairs[3].second, true, false);

            cells[0][6] = new CellUiState(pairs[4].second, true, false);
            cells[2][0] = new CellUiState(pairs[4].second, true, false);
            cells[3][7] = new CellUiState(pairs[4].second, true, false);
            cells[4][5] = new CellUiState(pairs[4].second, true, false);
            cells[5][1] = new CellUiState(pairs[4].second, true, false);

            cells[3][2] = new CellUiState(pairs[5].second, true, false);
            cells[4][4] = new CellUiState(pairs[5].second, true, false);
            cells[5][6] = new CellUiState(pairs[5].second, true, false);
            cells[7][5] = new CellUiState(pairs[5].second, true, false);
            cells[8][7] = new CellUiState(pairs[5].second, true, false);

            cells[0][1] = new CellUiState(pairs[6].second, true, false);
            cells[2][5] = new CellUiState(pairs[6].second, true, false);
            cells[3][3] = new CellUiState(pairs[6].second, true, false);
            cells[5][2] = new CellUiState(pairs[6].second, true, false);
            cells[6][4] = new CellUiState(pairs[6].second, true, false);

            cells[2][1] = new CellUiState(pairs[7].second, true, false);
            cells[7][4] = new CellUiState(pairs[7].second, true, false);

            cells[1][8] = new CellUiState(pairs[8].second, true, false);
            cells[3][6] = new CellUiState(pairs[8].second, true, false);
            cells[6][3] = new CellUiState(pairs[8].second, true, false);
            cells[7][7] = new CellUiState(pairs[8].second, true, false);
        }
        // 4x4
        else if (boardSize == 4 && subgridHeight == 4 && subgridWidth == 4) {
            // TODO
            cells[0][3] = new CellUiState(pairs[0].second, true, false);
            cells[2][1] = new CellUiState(pairs[0].second, true, false);

            cells[1][0] = new CellUiState(pairs[1].second, true, false);

            cells[0][0] = new CellUiState(pairs[2].second, true, false);
            cells[3][3] = new CellUiState(pairs[2].second, true, false);

            cells[1][3] = new CellUiState(pairs[3].second, true, false);
            cells[3][2] = new CellUiState(pairs[3].second, true, false);
        }
        // 6x6
        else if (boardSize == 6 && subgridHeight == 2 && subgridWidth == 3) {
            //TODO
            cells[0][3] = new CellUiState(pairs[0].second, true, false);
            cells[2][4] = new CellUiState(pairs[0].second, true, false);

            cells[1][1] = new CellUiState(pairs[1].second, true, false);
            cells[3][0] = new CellUiState(pairs[1].second, true, false);

            cells[0][5] = new CellUiState(pairs[2].second, true, false);
            cells[5][1] = new CellUiState(pairs[2].second, true, false);

            cells[2][2] = new CellUiState(pairs[3].second, true, false);
            cells[4][1] = new CellUiState(pairs[3].second, true, false);

            cells[3][5] = new CellUiState(pairs[4].second, true, false);
            cells[5][3] = new CellUiState(pairs[4].second, true, false);

            cells[0][2] = new CellUiState(pairs[5].second, true, false);
            cells[5][5] = new CellUiState(pairs[5].second, true, false);
        }
        // 12x12
        else if (boardSize == 12 && subgridHeight == 3 && subgridWidth == 4) {
            //TODO
            cells[0][0] = new CellUiState(pairs[0].second, true, false);
            cells[2][4] = new CellUiState(pairs[0].second, true, false);
            cells[3][11] = new CellUiState(pairs[0].second, true, false);
            cells[6][10] = new CellUiState(pairs[0].second, true, false);
            cells[8][2] = new CellUiState(pairs[0].second, true, false);
            cells[9][9] = new CellUiState(pairs[0].second, true, false);

            cells[1][9] = new CellUiState(pairs[1].second, true, false);
            cells[3][0] = new CellUiState(pairs[1].second, true, false);
            cells[4][8] = new CellUiState(pairs[1].second, true, false);
            cells[5][4] = new CellUiState(pairs[1].second, true, false);
            cells[7][7] = new CellUiState(pairs[1].second, true, false);
            cells[8][3] = new CellUiState(pairs[1].second, true, false);
            cells[11][2] = new CellUiState(pairs[1].second, true, false);

            cells[0][2] = new CellUiState(pairs[2].second, true, false);
            cells[3][1] = new CellUiState(pairs[2].second, true, false);
            cells[5][5] = new CellUiState(pairs[2].second, true, false);
            cells[7][8] = new CellUiState(pairs[2].second, true, false);
            cells[9][11] = new CellUiState(pairs[2].second, true, false);
            cells[10][7] = new CellUiState(pairs[2].second, true, false);

            cells[1][11] = new CellUiState(pairs[3].second, true, false);
            cells[2][7] = new CellUiState(pairs[3].second, true, false);
            cells[4][10] = new CellUiState(pairs[3].second, true, false);
            cells[7][9] = new CellUiState(pairs[3].second, true, false);
            cells[8][5] = new CellUiState(pairs[3].second, true, false);
            cells[9][0] = new CellUiState(pairs[3].second, true, false);

            cells[0][4] = new CellUiState(pairs[4].second, true, false);
            cells[2][8] = new CellUiState(pairs[4].second, true, false);
            cells[3][3] = new CellUiState(pairs[4].second, true, false);
            cells[6][2] = new CellUiState(pairs[4].second, true, false);
            cells[8][6] = new CellUiState(pairs[4].second, true, false);
            cells[10][9] = new CellUiState(pairs[4].second, true, false);

            cells[1][1] = new CellUiState(pairs[5].second, true, false);
            cells[3][4] = new CellUiState(pairs[5].second, true, false);
            cells[5][8] = new CellUiState(pairs[5].second, true, false);
            cells[6][3] = new CellUiState(pairs[5].second, true, false);
            cells[7][11] = new CellUiState(pairs[5].second, true, false);
            cells[9][2] = new CellUiState(pairs[5].second, true, false);
            cells[11][6] = new CellUiState(pairs[5].second, true, false);

            cells[1][2] = new CellUiState(pairs[6].second, true, false);
            cells[2][10] = new CellUiState(pairs[6].second, true, false);
            cells[3][5] = new CellUiState(pairs[6].second, true, false);
            cells[4][1] = new CellUiState(pairs[6].second, true, false);
            cells[7][0] = new CellUiState(pairs[6].second, true, false);
            cells[10][11] = new CellUiState(pairs[6].second, true, false);
            cells[11][7] = new CellUiState(pairs[6].second, true, false);

            cells[0][7] = new CellUiState(pairs[7].second, true, false);
            cells[5][10] = new CellUiState(pairs[7].second, true, false);
            cells[6][5] = new CellUiState(pairs[7].second, true, false);
            cells[7][1] = new CellUiState(pairs[7].second, true, false);
            cells[8][9] = new CellUiState(pairs[7].second, true, false);
            cells[9][4] = new CellUiState(pairs[7].second, true, false);
            cells[10][0] = new CellUiState(pairs[7].second, true, false);
            cells[11][8] = new CellUiState(pairs[7].second, true, false);

            cells[0][8] = new CellUiState(pairs[8].second, true, false);
            cells[2][0] = new CellUiState(pairs[8].second, true, false);
            cells[3][7] = new CellUiState(pairs[8].second, true, false);
            cells[4][3] = new CellUiState(pairs[8].second, true, false);
            cells[5][11] = new CellUiState(pairs[8].second, true, false);
            cells[6][6] = new CellUiState(pairs[8].second, true, false);
            cells[9][5] = new CellUiState(pairs[8].second, true, false);

            cells[1][5] = new CellUiState(pairs[9].second, true, false);
            cells[11][10] = new CellUiState(pairs[9].second, true, false);

            cells[0][10] = new CellUiState(pairs[10].second, true, false);
            cells[1][6] = new CellUiState(pairs[10].second, true, false);
            cells[6][8] = new CellUiState(pairs[10].second, true, false);
            cells[7][4] = new CellUiState(pairs[10].second, true, false);
            cells[10][3] = new CellUiState(pairs[10].second, true, false);

            cells[2][3] = new CellUiState(pairs[11].second, true, false);
            cells[4][6] = new CellUiState(pairs[11].second, true, false);
            cells[5][2] = new CellUiState(pairs[11].second, true, false);
            cells[10][4] = new CellUiState(pairs[11].second, true, false);
            cells[11][0] = new CellUiState(pairs[11].second, true, false);
        }

        mBoardUiState.setValue(new BoardUiState(
                boardSize, subgridHeight, subgridWidth,
                mBoardUiState.getValue().getSelectedRowIndex(),
                mBoardUiState.getValue().getSelectedColIndex(),
                cells
        ));
    }
}
