package ca.sfu.cmpt276.sudokulang.data.source;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Random;

import ca.sfu.cmpt276.sudokulang.data.BoardDimension;
import ca.sfu.cmpt276.sudokulang.data.BoardImpl;
import ca.sfu.cmpt276.sudokulang.data.source.local.BoardDao;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase;

// See: https://developer.android.com/codelabs/android-room-with-a-view#8
public class BoardRepositoryImpl implements BoardRepository {
    private final BoardDao mBoardDao;

    public BoardRepositoryImpl(Application application) {
        mBoardDao = GameDatabase.getDatabase(application).boardDao();
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
    public LiveData<List<BoardDimension>> getAvailableBoardDimensionsByLevel(String level) {
        return mBoardDao.getAvailableBoardDimensionsByLevel(level);
    }

    @Override
    public LiveData<List<String>> getAvailableBoardLevels() {
        return mBoardDao.getAvailableBoardLevels();
    }

    @Override
    public LiveData<List<String>> getAvailableBoardLevelsByDimension(int boardSize, int subgridHeight, int subgridWidth) {
        return mBoardDao.getAvailableBoardLevelsByDimension(boardSize, subgridHeight, subgridWidth);
    }

    @Override
    public BoardImpl getARandomBoardMatching(int boardSize, int subgridHeight, int subgridWidth, String level) {
        final var boards = mBoardDao.getAllFilteredBoards(boardSize, subgridHeight, subgridWidth, level);
        return boards.get(new Random().nextInt(boards.size()));
    }
}