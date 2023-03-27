package ca.sfu.cmpt276.sudokulang.data.source;

import androidx.lifecycle.LiveData;

import java.util.Collection;
import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Translation;
import ca.sfu.cmpt276.sudokulang.data.WordPair;

public interface TranslationRepository {
    List<WordPair> getNRandomWordPairsMatching(int n, String nativeLang, String learningLang, String langLevel);

    LiveData<List<String>> getAvailableLearningLanguages();

    LiveData<List<String>> getAvailableNativeLanguages();

    LiveData<List<String>> getAvailableLanguageLevels();

    List<Translation> getTranslationsByIds(long[] translationIds);

    Collection<Object> getAvailableLearningLanguage();
}