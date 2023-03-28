package ca.sfu.cmpt276.sudokulang.data.source;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Random;

import ca.sfu.cmpt276.sudokulang.data.BoardDimension;
import ca.sfu.cmpt276.sudokulang.data.BoardImpl;
import ca.sfu.cmpt276.sudokulang.data.source.local.BoardDao;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase;

// See: https://developer.android.com/codelabs/android-room-with-a-view#8
public class BoardRepositoryImpl implements BoardRepository {
    private static volatile BoardRepositoryImpl sInstance;
    private final BoardDao mBoardDao;

    private BoardRepositoryImpl(Context context) {
        final var db = GameDatabase.getDatabase(context);
        mBoardDao = db.boardDao();
    }

    public static BoardRepositoryImpl getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BoardRepositoryImpl.class) {
                if (sInstance == null) {
                    sInstance = new BoardRepositoryImpl(context);
                }
            }
        }
        return sInstance;
    }

    // Cite: https://stackoverflow.com/questions/1250643
    @Override
    public BoardImpl getBoardById(long id) {
        return mBoardDao.getBoardById(id);
    }

    @Override
    public LiveData<List<BoardDimension>> getAvailableBoardDimensions() {
        return mBoardDao.getAvailableBoardDimensions();
    }

    @Override
    public List<BoardDimension> getAvailableBoardDimensionsByLevel(String level) {
        return mBoardDao.getAvailableBoardDimensionsByLevel(level);
    }

    @Override
    public LiveData<List<String>> getAvailableBoardLevels() {
        return mBoardDao.getAvailableBoardLevels();
    }

    @Override
    public List<String> getAvailableBoardLevelsByDimension(int boardSize, int subgridHeight, int subgridWidth) {
        return mBoardDao.getAvailableBoardLevelsByDimension(boardSize, subgridHeight, subgridWidth);
    }

    @Override
    public BoardImpl getARandomBoardMatching(int boardSize, int subgridHeight, int subgridWidth, String level) {
        final var boards = mBoardDao.getAllFilteredBoards(boardSize, subgridHeight, subgridWidth, level);
        if (boards.isEmpty()) {
            throw new IllegalStateException("No matches found in database");
        }
        return boards.get(new Random().nextInt(boards.size()));
    }
}