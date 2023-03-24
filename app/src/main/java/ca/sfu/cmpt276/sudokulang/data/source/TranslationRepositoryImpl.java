package ca.sfu.cmpt276.sudokulang.data.source;

import static ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase.databaseWriteExecutor;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import ca.sfu.cmpt276.sudokulang.data.Translation;
import ca.sfu.cmpt276.sudokulang.data.WordPair;
import ca.sfu.cmpt276.sudokulang.data.source.local.GameDatabase;
import ca.sfu.cmpt276.sudokulang.data.source.local.TranslationDao;

public class TranslationRepositoryImpl implements TranslationRepository {
    private final TranslationDao mTranslationDao;

    public TranslationRepositoryImpl(Application application) {
        mTranslationDao = GameDatabase.getDatabase(application).translationDao();
    }

    @Override
    public List<WordPair> getNRandomWordPairsMatching(int n, String nativeLang, String learningLang, String langLevel) {
        final var pairsAtomic = new AtomicReference<List<WordPair>>();
        databaseWriteExecutor.execute(() ->
                pairsAtomic.set(mTranslationDao.getAllFilteredWordPairs(nativeLang, learningLang, langLevel)));
        databaseWriteExecutor.shutdown();
        try {
            databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // re-interrupt the thread and propagate the exception
            Thread.currentThread().interrupt();
        }
        final var pairs = pairsAtomic.get();
        Collections.shuffle(pairs);
        return pairs.stream().limit(n).collect(Collectors.toList());
    }

    @Override
    public LiveData<List<String>> getAvailableLearningLanguages() {
        return mTranslationDao.getAvailableLearningLanguages();
    }

    @Override
    public LiveData<List<String>> getAvailableNativeLanguages() {
        return mTranslationDao.getAvailableNativeLanguages();
    }

    @Override
    public LiveData<List<String>> getAvailableLanguageLevels() {
        return mTranslationDao.getAvailableLanguageLevels();
    }

    @Override
    public List<Translation> getTranslationsByIds(long[] translationIds) {
        final var translationsAtomic = new AtomicReference<List<Translation>>();
        databaseWriteExecutor.execute(() ->
                translationsAtomic.set(mTranslationDao.getTranslationsByIds(translationIds)));
        databaseWriteExecutor.shutdown();
        try {
            databaseWriteExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // re-interrupt the thread and propagate the exception
            Thread.currentThread().interrupt();
        }
        return translationsAtomic.get();
    }
}