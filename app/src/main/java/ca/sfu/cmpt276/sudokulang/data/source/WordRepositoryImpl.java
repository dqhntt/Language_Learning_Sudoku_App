package ca.sfu.cmpt276.sudokulang.data.source;

import static ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase.databaseWriteExecutor;

import android.app.Application;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
        final var longAtomic = new AtomicReference<Long>();
        databaseWriteExecutor.execute(() ->
                longAtomic.set(mWordDao.getIdOfWord(word)));
        databaseWriteExecutor.shutdown();
        try {
            databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // re-interrupt the thread and propagate the exception
            Thread.currentThread().interrupt();
        }
        return longAtomic.get();
    }

    @Override
    public void insert(Word word) {
        GameDatabase.Util.execute(() -> mWordDao.insert(word));
    }

    @Override
    public void insert(List<Word> words) {
        GameDatabase.Util.execute(() -> mWordDao.insert(words));
    }

    @Override
    public long getIdOfLanguage(String language) {
        final var longAtomic = new AtomicReference<Long>();
        databaseWriteExecutor.execute(() ->
                longAtomic.set(mLanguageDao.getIdOfLanguage(language)));
        databaseWriteExecutor.shutdown();
        try {
            databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // re-interrupt the thread and propagate the exception
            Thread.currentThread().interrupt();
        }
        return longAtomic.get();
    }

    @Override
    public long getIdOfLanguageLevel(String languageLevel) {
        final var longAtomic = new AtomicReference<Long>();
        databaseWriteExecutor.execute(() ->
                longAtomic.set(mLanguageDao.getIdOfLanguageLevel(languageLevel)));
        databaseWriteExecutor.shutdown();
        try {
            databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // re-interrupt the thread and propagate the exception
            Thread.currentThread().interrupt();
        }
        return longAtomic.get();
    }
}