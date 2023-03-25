package ca.sfu.cmpt276.sudokulang;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.sfu.cmpt276.sudokulang.data.Cell;
import ca.sfu.cmpt276.sudokulang.data.CellImpl;
import ca.sfu.cmpt276.sudokulang.data.WordPair;
import ca.sfu.cmpt276.sudokulang.data.source.BoardRepository;
import ca.sfu.cmpt276.sudokulang.data.source.BoardRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.data.source.GameRepository;
import ca.sfu.cmpt276.sudokulang.data.source.GameRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepository;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.data.source.WordRepository;
import ca.sfu.cmpt276.sudokulang.data.source.WordRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.ui.game.board.BoardUiState;

/**
 * State processor for UI elements in GameFragment.
 *
 * @cite <a href="https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-4-saving-user-data/lesson-10-storing-data-with-room/10-1-c-room-livedata-viewmodel/10-1-c-room-livedata-viewmodel.html#viewmodel">LiveData & ViewModel</a>
 */
public class GameViewModel extends AndroidViewModel {
    public final BoardRepository boardRepo;
    public final GameRepository gameRepo;
    public final TranslationRepository translationRepo;
    public final WordRepository wordRepo;
    private final @NonNull MutableLiveData<BoardUiState> mBoardUiState;
    private final boolean mGameInProgress;
    private final @NonNull Map<String, WordPair> mWordToWordPairMap = new HashMap<>();
    private final @NonNull BiMap<Integer, WordPair> mValueWordPairBiMap = HashBiMap.create();
    private WordPair[] mWordPairs = null;

    /**
     * @implNote Board dimension when default constructed is undefined. <p>
     * Use {@link #generateNewBoard(int, int, int)} to set a desired dimension.
     */
    public GameViewModel(Application app) {
        super(app);
        boardRepo = new BoardRepositoryImpl(app);
        gameRepo = new GameRepositoryImpl(app);
        translationRepo = new TranslationRepositoryImpl(app);
        wordRepo = new WordRepositoryImpl(app);
        mBoardUiState = new MutableLiveData<>();
        mGameInProgress = true;
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
        final var selectedCell = (CellImpl) mBoardUiState.getValue().getSelectedCell();
        assert selectedCell != null;
        if (selectedCell.isPrefilled()) {
            return;
        }
        setSelectedCellState(
                selectedCell
                        .setValue(mValueWordPairBiMap.inverse().get(mWordToWordPairMap.get(buttonValue)))
                        .setText(buttonValue)
                        .setErrorCell(!isValidValueForCell(
                                buttonValue,
                                mBoardUiState.getValue().getSelectedRowIndex(),
                                mBoardUiState.getValue().getSelectedColIndex()
                        ))
        );
    }

    public void clearSelectedCell() {
        setSelectedCellState(new CellImpl());
    }

    private void setSelectedCellState(@NonNull Cell newState) {
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
        final var newCells = new Cell[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                newCells[i][j] = new CellImpl();
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
        for (var pair : getWordPairs()) {
            map.put(pair.getOriginalWord(), pair.getTranslatedWord());
        }
        return map;
    }

    public WordPair[] getWordPairs() {
        if (mWordPairs == null || !mGameInProgress) {
            generateWordPairs();
        }
        return mWordPairs;
    }

    private void generateWordPairs() {
        mWordPairs = translationRepo.getNRandomWordPairsMatching(
                mBoardUiState.getValue().getBoardSize(),
                "English", "French", "Beginner"
        ).toArray(new WordPair[0]);
        prepareMaps();
    }

    private void prepareMaps() {
        final List<Integer> values = new ArrayList<>();
        for (int i = 0; i < mWordPairs.length; i++) {
            values.add(i + 1);
        }
        Collections.shuffle(values);
        mValueWordPairBiMap.clear();
        for (int i = 0; i < mWordPairs.length; i++) {
            mValueWordPairBiMap.put(values.get(i), mWordPairs[i]);
        }
        mWordToWordPairMap.clear();
        for (var pair : mWordPairs) {
            mWordToWordPairMap.put(pair.getOriginalWord(), pair);
            mWordToWordPairMap.put(pair.getTranslatedWord(), pair);
        }
    }

    private void generateBoardData() {
        // For TESTING only.
        // TODO: Replace this with a database. !!

        final int boardSize = mBoardUiState.getValue().getBoardSize();
        final int subgridHeight = mBoardUiState.getValue().getSubgridHeight();
        final int subgridWidth = mBoardUiState.getValue().getSubgridWidth();

        generateWordPairs();
        final var cells = Arrays.stream(mBoardUiState.getValue().getCells())
                .map(row -> Arrays.stream(row)
                        .map(cell -> (CellImpl) cell)
                        .toArray(CellImpl[]::new))
                .toArray(CellImpl[][]::new);

        // 9x9
        if (boardSize == 9 && subgridHeight == 3 && subgridWidth == 3) {
            cells[0][3].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][6].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][7].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][5].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][1].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][2].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[1][4].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][1].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][7].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][0].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][1].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][2].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][8].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][3].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[8][6].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][6].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][4].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][1].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][7].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][2].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][6].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][0].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][7].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][5].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][1].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);

            cells[3][2].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][4].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][6].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][5].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[8][7].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][1].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][5].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][3].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][2].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][4].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);

            cells[2][1].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][4].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][8].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][6].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][3].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][7].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
        }
        // 4x4
        else if (boardSize == 4 && subgridHeight == 4 && subgridWidth == 4) {
            cells[0][3].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][1].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][0].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][0].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][3].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][3].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][2].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
        }
        // 6x6
        else if (boardSize == 6 && subgridHeight == 2 && subgridWidth == 3) {
            cells[0][3].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][4].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][1].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][0].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][5].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][1].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);

            cells[2][2].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][1].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);

            cells[3][5].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][3].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][2].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][5].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
        }
        // 12x12
        else if (boardSize == 12 && subgridHeight == 3 && subgridWidth == 4) {
            cells[0][0].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][4].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][11].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][10].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[8][2].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);
            cells[9][9].setValue(1)
                    .setText(mValueWordPairBiMap.get(1).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][9].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][0].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][8].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][4].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][7].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[8][3].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);
            cells[11][2].setValue(2)
                    .setText(mValueWordPairBiMap.get(2).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][2].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][1].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][5].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][8].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[9][11].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);
            cells[10][7].setValue(3)
                    .setText(mValueWordPairBiMap.get(3).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][11].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][7].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][10].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][9].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[8][5].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);
            cells[9][0].setValue(4)
                    .setText(mValueWordPairBiMap.get(4).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][4].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][8].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][3].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][2].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[8][6].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);
            cells[10][9].setValue(5)
                    .setText(mValueWordPairBiMap.get(5).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][1].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][4].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][8].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][3].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][11].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[9][2].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);
            cells[11][6].setValue(6)
                    .setText(mValueWordPairBiMap.get(6).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][2].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][10].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][5].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][1].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][0].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[10][11].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);
            cells[11][7].setValue(7)
                    .setText(mValueWordPairBiMap.get(7).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][7].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][10].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][5].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][1].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);
            cells[8][9].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);
            cells[9][4].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);
            cells[10][0].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);
            cells[11][8].setValue(8)
                    .setText(mValueWordPairBiMap.get(8).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][8].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[2][0].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[3][7].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][3].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][11].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][6].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);
            cells[9][5].setValue(9)
                    .setText(mValueWordPairBiMap.get(9).getTranslatedWord())
                    .setPrefilled(true);

            cells[1][5].setValue(10)
                    .setText(mValueWordPairBiMap.get(10).getTranslatedWord())
                    .setPrefilled(true);
            cells[11][10].setValue(10)
                    .setText(mValueWordPairBiMap.get(10).getTranslatedWord())
                    .setPrefilled(true);

            cells[0][10].setValue(11)
                    .setText(mValueWordPairBiMap.get(11).getTranslatedWord())
                    .setPrefilled(true);
            cells[1][6].setValue(11)
                    .setText(mValueWordPairBiMap.get(11).getTranslatedWord())
                    .setPrefilled(true);
            cells[6][8].setValue(11)
                    .setText(mValueWordPairBiMap.get(11).getTranslatedWord())
                    .setPrefilled(true);
            cells[7][4].setValue(11)
                    .setText(mValueWordPairBiMap.get(11).getTranslatedWord())
                    .setPrefilled(true);
            cells[10][3].setValue(11)
                    .setText(mValueWordPairBiMap.get(11).getTranslatedWord())
                    .setPrefilled(true);

            cells[2][3].setValue(12)
                    .setText(mValueWordPairBiMap.get(12).getTranslatedWord())
                    .setPrefilled(true);
            cells[4][6].setValue(12)
                    .setText(mValueWordPairBiMap.get(12).getTranslatedWord())
                    .setPrefilled(true);
            cells[5][2].setValue(12)
                    .setText(mValueWordPairBiMap.get(12).getTranslatedWord())
                    .setPrefilled(true);
            cells[10][4].setValue(12)
                    .setText(mValueWordPairBiMap.get(12).getTranslatedWord())
                    .setPrefilled(true);
            cells[11][0].setValue(12)
                    .setText(mValueWordPairBiMap.get(12).getTranslatedWord())
                    .setPrefilled(true);
        }

        mBoardUiState.setValue(new BoardUiState(
                boardSize, subgridHeight, subgridWidth,
                mBoardUiState.getValue().getSelectedRowIndex(),
                mBoardUiState.getValue().getSelectedColIndex(),
                cells
        ));
    }
}