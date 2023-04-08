package ca.sfu.cmpt276.sudokulang.data.source;

import static ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase.databaseWriteExecutor;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Game;
import ca.sfu.cmpt276.sudokulang.data.GameTranslation;
import ca.sfu.cmpt276.sudokulang.data.GameWithTranslations;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDao;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameTranslationDao;

public class GameRepositoryImpl implements GameRepository {
    private static volatile GameRepositoryImpl sInstance;
    private final GameDao mGameDao;
    private final GameTranslationDao mGameTranslationDao;

    private GameRepositoryImpl(Context context) {
        final var db = GameDatabase.getDatabase(context);
        mGameDao = db.gameDao();
        mGameTranslationDao = db.gameTranslationDao();
    }

    public static GameRepositoryImpl getInstance(Context context) {
        if (sInstance == null) {
            synchronized (GameRepositoryImpl.class) {
                if (sInstance == null) {
                    sInstance = new GameRepositoryImpl(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public long insert(@NonNull Game game) {
        return mGameDao.insert(game);
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