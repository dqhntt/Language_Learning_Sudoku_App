package ca.sfu.cmpt276.sudokulang;

import static ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase.databaseWriteExecutor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ca.sfu.cmpt276.sudokulang.data.Board;
import ca.sfu.cmpt276.sudokulang.data.BoardImpl;
import ca.sfu.cmpt276.sudokulang.data.Cell;
import ca.sfu.cmpt276.sudokulang.data.CellImpl;
import ca.sfu.cmpt276.sudokulang.data.Game;
import ca.sfu.cmpt276.sudokulang.data.GameTranslation;
import ca.sfu.cmpt276.sudokulang.data.WordPair;
import ca.sfu.cmpt276.sudokulang.data.source.BoardRepository;
import ca.sfu.cmpt276.sudokulang.data.source.BoardRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.data.source.GameRepository;
import ca.sfu.cmpt276.sudokulang.data.source.GameRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepository;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepositoryImpl;

/**
 * State processor for UI elements in GameFragment.
 *
 * @cite <a href="https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-4-saving-user-data/lesson-10-storing-data-with-room/10-1-c-room-livedata-viewmodel/10-1-c-room-livedata-viewmodel.html#viewmodel">LiveData & ViewModel</a>
 */
public class GameViewModel extends AndroidViewModel {
    private final BoardRepository mBoardRepo;
    private final GameRepository mGameRepo;
    private final TranslationRepository mTranslationRepo;
    private final @NonNull MutableLiveData<Board> mBoardUiState;
    private final @NonNull MutableLiveData<Boolean> mGameInProgress;
    private final @NonNull MutableLiveData<WordPair[]> mWordPairs;
    private final @NonNull Map<Integer, WordPair> mValueWordPairMap = new HashMap<>();
    private final @NonNull Map<String, Integer> mOriginalWordValueMap = new HashMap<>();
    private final @NonNull Map<String, String> mButtonCellTextMap = new HashMap<>();
    private Game mCurrentGame = null;
    private long mPauseStartTime, mTotalPausedTime;

    /**
     * @implNote Board dimension when default constructed is undefined. <p>
     * @see #startNewGame(String, String, String, String, int, int, int)
     */
    public GameViewModel(Application app) {
        super(app);
        mBoardRepo = BoardRepositoryImpl.getInstance(app.getApplicationContext());
        mGameRepo = GameRepositoryImpl.getInstance(app.getApplicationContext());
        mTranslationRepo = TranslationRepositoryImpl.getInstance(app.getApplicationContext());
        mBoardUiState = new MutableLiveData<>();
        mGameInProgress = new MutableLiveData<>();
        mWordPairs = new MutableLiveData<>();
        mWordPairs.observeForever(p -> prepareMaps());
        mBoardUiState.observeForever(board -> updateGameInDatabase(mCurrentGame, board));
    }

    public LiveData<Board> getBoardUiState() {
        return mBoardUiState;
    }

    /**
     * @param nativeLang        Language the player already knows.
     * @param learningLang      Language the player wants to learn.
     * @param learningLangLevel Player's current level of {@code learningLang}.
     * @param sudokuLevel       How well the player already knows Sudoku.
     * @param boardSize         Number of cells in each column or row.
     * @param subgridHeight     Number of cells in each sub-grid's column.
     *                          Equals {@code boardSize} if no sub-grid.
     * @param subgridWidth      Number of cells in each sub-grid's row.
     *                          Equals {@code boardSize} if no sub-grid.
     */
    public void startNewGame(String nativeLang, String learningLang, String learningLangLevel,
                             String sudokuLevel, int boardSize, int subgridHeight, int subgridWidth) {
        if (!isValidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
            throw new IllegalArgumentException("Invalid board dimension");
        }
        final var newBoard =
                mBoardRepo.getARandomBoardMatching(boardSize, subgridHeight, subgridWidth, sudokuLevel);
        final var wordPairs =
                mTranslationRepo.getNRandomWordPairsMatching(
                        boardSize, nativeLang, learningLang, learningLangLevel
                ).toArray(new WordPair[0]);
        mWordPairs.setValue(wordPairs);
        setTextToCells(newBoard);
        mCurrentGame = new Game(mGameRepo.generateId(), newBoard.getId(), newBoard.getCells());
        insertGameToDatabase(mCurrentGame, wordPairs);
        mGameInProgress.setValue(true);
        mBoardUiState.setValue(newBoard);
    }

    private void setTextToCells(@NonNull Board newBoard) {
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
    }

    private void insertGameToDatabase(@NonNull Game game, @NonNull WordPair... wordPairs) {
        databaseWriteExecutor.execute(() -> {
            mGameRepo.insert(game);
            mGameRepo.insert(Arrays.stream(wordPairs)
                    .map(pair -> new GameTranslation(game.getId(), pair.getTranslationId()))
                    .collect(Collectors.toList())
            );
        });
    }

    private void updateGameInDatabase(@Nullable Game game, @NonNull Board board) {
        databaseWriteExecutor.execute(() -> {
            if (game != null && mGameInProgress.getValue()) {
                mGameRepo.update(game
                        .setCompleted(board.isSolvedBoard())
                        .setCurrentBoardValues(board.getCells())
                        .setTimeDuration(getElapsedTime())
                );
            }
        });
    }

    @NonNull
    public LiveData<Boolean> isGameInProgress() {
        return mGameInProgress;
    }

    public void endGame() {
        mGameInProgress.setValue(false);
    }

    /**
     * @return The elapsed time in milliseconds.
     */
    public long getElapsedTime() {
        assert mCurrentGame != null;
        return System.currentTimeMillis()
                - mCurrentGame.getStartTime().getTime()
                - mTotalPausedTime;
    }

    /**
     * @post {@link #isGameInProgress()} {@code == false}
     */
    public void pauseGame() {
        mPauseStartTime = System.currentTimeMillis();
        mGameInProgress.setValue(false);
    }

    /**
     * @post {@link #isGameInProgress()} {@code == true}
     */
    public void resumeGame() {
        mTotalPausedTime += (System.currentTimeMillis() - mPauseStartTime);
        mGameInProgress.setValue(true);
    }

    public void resetGame() {
        mCurrentGame.setStartTime(new Date());
        mTotalPausedTime = 0;
        final var resetBoard = ((BoardImpl) mBoardUiState.getValue()).resetBoard();
        setTextToCells(resetBoard);
        mBoardUiState.setValue(resetBoard);
        mGameInProgress.setValue(true);
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
                        .setValue(mOriginalWordValueMap.get(buttonText))
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

    @NonNull
    public LiveData<WordPair[]> getWordPairs() {
        if (mWordPairs.getValue() == null) {
            throw new IllegalStateException("Game must be created before retrieving the word pairs");
        }
        return mWordPairs;
    }

    private void prepareMaps() {
        final List<Integer> values = new ArrayList<>();
        final var wordPairs = mWordPairs.getValue();
        assert wordPairs != null;
        for (int i = 0; i < wordPairs.length; i++) {
            values.add(i + 1);
        }
        Collections.shuffle(values);
        mValueWordPairMap.clear();
        mOriginalWordValueMap.clear();
        for (int i = 0; i < wordPairs.length; i++) {
            final var currPair = wordPairs[i];
            final var currVal = values.get(i);
            mValueWordPairMap.put(currVal, currPair);
            mOriginalWordValueMap.put(currPair.getOriginalWord(), currVal);
        }
        mButtonCellTextMap.clear();
        for (var pair : wordPairs) {
            mButtonCellTextMap.put(pair.getOriginalWord(), pair.getTranslatedWord());
        }
    }
}