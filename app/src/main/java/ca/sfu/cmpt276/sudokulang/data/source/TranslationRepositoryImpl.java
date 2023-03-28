package ca.sfu.cmpt276.sudokulang.data.source;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.List;
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
    public List<WordPair> getNRandomWordPairsMatching(int n,
                                                      String nativeLang, String learningLang, String langLevel) {
        final var pairs = mTranslationDao.getAllFilteredWordPairs(nativeLang, learningLang, langLevel);
        if (pairs.isEmpty()) {
            throw new IllegalStateException("No matches found in database");
        }
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
        return mTranslationDao.getTranslationsByIds(translationIds);
    }
}