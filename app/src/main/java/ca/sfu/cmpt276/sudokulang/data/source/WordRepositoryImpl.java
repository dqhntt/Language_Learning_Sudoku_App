package ca.sfu.cmpt276.sudokulang.data.source;

import static ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase.databaseWriteExecutor;

import android.content.Context;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Language;
import ca.sfu.cmpt276.sudokulang.data.Word;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase;
import ca.sfu.cmpt276.sudokulang.data.source.local.LanguageDao;
import ca.sfu.cmpt276.sudokulang.data.source.local.WordDao;

public class WordRepositoryImpl implements WordRepository {
    private static volatile WordRepositoryImpl sInstance;
    public final LanguageDao mLanguageDao;
    public final WordDao mWordDao;

    private WordRepositoryImpl(Context context) {
        final var db = GameDatabase.getDatabase(context);
        mLanguageDao = db.languageDao();
        mWordDao = db.wordDao();
    }

    public static WordRepositoryImpl getInstance(Context context) {
        if (sInstance == null) {
            synchronized (WordRepositoryImpl.class) {
                if (sInstance == null) {
                    sInstance = new WordRepositoryImpl(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void insert(Word word) {
        databaseWriteExecutor.execute(() -> mWordDao.insert(word));
    }

    @Override
    public void insert(List<Word> words) {
        databaseWriteExecutor.execute(() -> mWordDao.insert(words));
    }

    @Override
    public Language getLanguageByName(String languageName) {
        return mLanguageDao.getLanguageByName(languageName);
    }
}