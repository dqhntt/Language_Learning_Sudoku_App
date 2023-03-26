package ca.sfu.cmpt276.sudokulang;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.sfu.cmpt276.sudokulang.data.Board;
import ca.sfu.cmpt276.sudokulang.data.BoardImpl;
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
    private final @NonNull MutableLiveData<Board> mBoardUiState;
    private final boolean mGameInProgress;
    private final @NonNull Map<Integer, WordPair> mValueWordPairMap = new HashMap<>();
    private final @NonNull Map<String, String> mButtonCellTextMap = new HashMap<>();
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

    public LiveData<Board> getBoardUiState() {
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
        if (!isValidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
            throw new IllegalArgumentException("Invalid board dimension");
        }
        final var newBoard =
                boardRepo.getARandomBoardMatching(
                        boardSize, subgridHeight, subgridWidth,
                        boardSize == 12 ? "Expert" : "Novice" // TODO: Replace this with a proper check.
                );
        generateWordPairs(newBoard.getBoardSize());
        for (var row : newBoard.getCells()) {
            for (var cell : row) {
                final var currCell = (CellImpl) cell;
                final var currValue = currCell.getValue();
                currCell.setText(currValue == 0
                        ? ""
                        : mValueWordPairMap.get(currValue).getTranslatedWord()
                );
            }
        }
        mBoardUiState.setValue(newBoard);
    }

    /**
     * Set a cell as selected.
     *
     * @param rowIndex Row index of the cell, -1 if none.
     * @param colIndex Column index of the cell, -1 if none.
     */
    public void setSelectedCell(int rowIndex, int colIndex) {
        mBoardUiState.setValue(
                ((BoardImpl) mBoardUiState.getValue()).setSelectedIndexes(rowIndex, colIndex)
        );
    }

    public void setNoSelectedCell() {
        setSelectedCell(-1, -1);
    }

    public void setSelectedCellText(@NonNull String buttonText) {
        final var selectedCell = (CellImpl) mBoardUiState.getValue().getSelectedCell();
        assert selectedCell != null;
        if (selectedCell.isPrefilled()) {
            return;
        }
        setSelectedCellState(
                selectedCell
                        .setText(buttonText)
                        .setErrorCell(!isValidValueForCell(
                                buttonText,
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
            mBoardUiState.setValue(mBoardUiState.getValue());
        }
    }

    private boolean isValidBoardDimension(int boardSize, int subgridHeight, int subgridWidth) {
        // Ensure sub-grids are equally divided.
        return (subgridHeight > 0 && subgridHeight <= boardSize) && (boardSize % subgridHeight == 0)
                && (subgridWidth > 0 && subgridWidth <= boardSize) && (boardSize % subgridWidth == 0);
    }

    private boolean isValidValueForCell(@NonNull String buttonText, int rowIndex, int colIndex) {
        final int boardSize = mBoardUiState.getValue().getBoardSize();
        final int subgridHeight = mBoardUiState.getValue().getSubgridHeight();
        final int subgridWidth = mBoardUiState.getValue().getSubgridWidth();
        if (!areValidIndexes(rowIndex, colIndex)) {
            return false;
        }
        if (subgridWidth == boardSize && subgridHeight == boardSize) {
            return isValidValueForCellInRowAndColumn(buttonText, rowIndex, colIndex);
        } else {
            return isValidValueForCellInRowAndColumn(buttonText, rowIndex, colIndex)
                    && isValidValueForCellInSubgrid(buttonText, rowIndex, colIndex);
        }
    }

    private boolean areValidIndexes(int rowIndex, int colIndex) {
        return (rowIndex >= 0 && rowIndex < mBoardUiState.getValue().getBoardSize())
                && (colIndex >= 0 && colIndex < mBoardUiState.getValue().getBoardSize());
    }

    private boolean isValidValueForCellInRowAndColumn(@NonNull String buttonText, int rowIndex, int colIndex) {
        final int boardSize = mBoardUiState.getValue().getBoardSize();
        final var cells = mBoardUiState.getValue().getCells();
        for (int i = 0; i < boardSize; i++) {
            if (i == rowIndex) {
                continue;
            }
            final var cellText = cells[i][colIndex].getText();
            if (cellText.equals(buttonText)
                    || cellText.equals(mButtonCellTextMap.get(buttonText))) {
                return false;
            }
        }
        for (int j = 0; j < boardSize; j++) {
            if (j == colIndex) {
                continue;
            }
            final var cellText = cells[rowIndex][j].getText();
            if (cellText.equals(buttonText)
                    || cellText.equals(mButtonCellTextMap.get(buttonText))) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidValueForCellInSubgrid(@NonNull String buttonText, int rowIndex, int colIndex) {
        final int subgridHeight = mBoardUiState.getValue().getSubgridHeight();
        final int subgridWidth = mBoardUiState.getValue().getSubgridWidth();
        final int startRowIndex = rowIndex - rowIndex % subgridHeight;
        final int startColIndex = colIndex - colIndex % subgridWidth;
        final int endRowIndex = startRowIndex + subgridHeight - 1;
        final int endColIndex = startColIndex + subgridWidth - 1;
        for (int i = startRowIndex; i <= endRowIndex; i++) {
            for (int j = startColIndex; j <= endColIndex; j++) {
                if (i == rowIndex && j == colIndex) {
                    continue;
                }
                final var cellText = mBoardUiState.getValue().getCells()[i][j].getText();
                if (cellText.equals(buttonText)
                        || cellText.equals(mButtonCellTextMap.get(buttonText))) {
                    return false;
                }
            }
        }
        return true;
    }

    public WordPair[] getWordPairs() {
        if (mWordPairs == null || !mGameInProgress) {
            generateWordPairs(mBoardUiState.getValue().getBoardSize());
        }
        return mWordPairs;
    }

    private void generateWordPairs(int boardSize) {
        mWordPairs = translationRepo.getNRandomWordPairsMatching(
                boardSize,
                // TODO: Get these values from Activity/Fragment.
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
        mValueWordPairMap.clear();
        for (int i = 0; i < mWordPairs.length; i++) {
            mValueWordPairMap.put(values.get(i), mWordPairs[i]);
        }
        mButtonCellTextMap.clear();
        for (var pair : mWordPairs) {
            mButtonCellTextMap.put(pair.getOriginalWord(), pair.getTranslatedWord());
        }
    }
}