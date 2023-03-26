package ca.sfu.cmpt276.sudokulang.data.source;

import static ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase.databaseWriteExecutor;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Game;
import ca.sfu.cmpt276.sudokulang.data.GameTranslation;
import ca.sfu.cmpt276.sudokulang.data.GameWithTranslations;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDao;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameTranslationDao;

public class GameRepositoryImpl implements GameRepository {
    private final GameDao mGameDao;
    private final GameTranslationDao mGameTranslationDao;

    public GameRepositoryImpl(Application application) {
        final var db = GameDatabase.getDatabase(application);
        mGameDao = db.gameDao();
        mGameTranslationDao = db.gameTranslationDao();
    }

    @Override
    public long generateId() {
        return mGameDao.generateId();
    }

    @Override
    public void insert(@NonNull Game game) {
        databaseWriteExecutor.execute(() -> mGameDao.insert(game));
    }

    @Override
    public void update(@NonNull Game game) {
        databaseWriteExecutor.execute(() -> mGameDao.update(game));
    }

    @NonNull
    @Override
    public List<GameWithTranslations> getAllGamesWithTranslations() {
        return mGameTranslationDao.getAllGamesWithTranslations();
    }

    @Override
    public void insert(@NonNull GameTranslation gameTranslation) {
        databaseWriteExecutor.execute(() -> mGameTranslationDao.insert(gameTranslation));
    }

    @Override
    public void insert(@NonNull List<GameTranslation> gameTranslations) {
        databaseWriteExecutor.execute(() -> mGameTranslationDao.insert(gameTranslations));
    }
}