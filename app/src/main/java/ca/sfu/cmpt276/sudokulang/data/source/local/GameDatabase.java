package ca.sfu.cmpt276.sudokulang.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.sfu.cmpt276.sudokulang.data.BoardImpl;
import ca.sfu.cmpt276.sudokulang.data.Converters;
import ca.sfu.cmpt276.sudokulang.data.Game;
import ca.sfu.cmpt276.sudokulang.data.GameTranslation;
import ca.sfu.cmpt276.sudokulang.data.Language;
import ca.sfu.cmpt276.sudokulang.data.LanguageLevel;
import ca.sfu.cmpt276.sudokulang.data.Translation;
import ca.sfu.cmpt276.sudokulang.data.Word;

// A Room database using the singleton pattern.
// Cite: https://developer.android.com/codelabs/android-room-with-a-view#7
@Database(
        version = 20230405,
        entities = {
                BoardImpl.class,
                Game.class,
                GameTranslation.class,
                Language.class,
                LanguageLevel.class,
                Translation.class,
                Word.class
        }
)
@TypeConverters({Converters.class})
public abstract class GameDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 6;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile GameDatabase INSTANCE;

    public static GameDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GameDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GameDatabase.class, "game-database")
                            .createFromAsset("database/GameDatabase.db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract BoardDao boardDao();

    public abstract GameDao gameDao();

    public abstract GameTranslationDao gameTranslationDao();

    public abstract LanguageDao languageDao();

    public abstract TranslationDao translationDao();

    public abstract WordDao wordDao();
}