package ca.sfu.cmpt276.sudokulang.data.source;

import static ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase.databaseWriteExecutor;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
        final var boardAtomic = new AtomicReference<BoardImpl>();
        databaseWriteExecutor.execute(() -> boardAtomic.set(mBoardDao.getBoardById(id)));
        databaseWriteExecutor.shutdown();
        try {
            databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // re-interrupt the thread and propagate the exception
            Thread.currentThread().interrupt();
        }
        return boardAtomic.get();
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
        final var boardsAtomic = new AtomicReference<List<BoardImpl>>();
        databaseWriteExecutor.execute(() ->
                boardsAtomic.set(mBoardDao.getAllFilteredBoards(boardSize, subgridHeight, subgridWidth, level)));
        databaseWriteExecutor.shutdown();
        try {
            databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // re-interrupt the thread and propagate the exception
            Thread.currentThread().interrupt();
        }
        return boardsAtomic.get().get(new Random().nextInt(boardSize));
    }
}