package ca.sfu.cmpt276.sudokulang.data.source;

import static ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase.databaseWriteExecutor;

import android.app.Application;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Word;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase;
import ca.sfu.cmpt276.sudokulang.data.source.local.LanguageDao;
import ca.sfu.cmpt276.sudokulang.data.source.local.WordDao;

public class WordRepositoryImpl implements WordRepository {
    public final LanguageDao mLanguageDao;
    public final WordDao mWordDao;

    public WordRepositoryImpl(Application application) {
        final var db = GameDatabase.getDatabase(application);
        mLanguageDao = db.languageDao();
        mWordDao = db.wordDao();
    }

    @Override
    public long getIdOfWord(String word) {
        return mWordDao.getIdOfWord(word);
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
    public long getIdOfLanguage(String language) {
        return mLanguageDao.getIdOfLanguage(language);
    }

    @Override
    public long getIdOfLanguageLevel(String languageLevel) {
        return mLanguageDao.getIdOfLanguageLevel(languageLevel);
    }
}