package ca.sfu.cmpt276.sudokulang.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Translation;
import ca.sfu.cmpt276.sudokulang.data.WordPair;

@Dao
public interface TranslationDao {
    @Query("SELECT t.id AS translationId, w1.text AS originalWord, w2.text AS translatedWord " +
            "FROM translation AS t " +
            "  JOIN word AS w1 ON w1.id = t.original_word_id " +
            "  JOIN language AS l1 ON l1.id = w1.language_id " +
            "  JOIN word AS w2 ON w2.id = t.translated_word_id " +
            "  JOIN language AS l2 ON l2.id = w2.language_id " +
            "  JOIN language_level AS lvl ON lvl.id = w2.language_level_id " +
            "WHERE w1.language_level_id = w2.language_level_id " +
            "  AND lvl.name = :langLevel" +
            "  AND l1.name = :nativeLang " +
            "  AND l2.name = :learningLang")
    List<WordPair> getAllFilteredWordPairs(String nativeLang, String learningLang, String langLevel);

    @Query("SELECT DISTINCT l.name " +
            "FROM translation AS t " +
            "  JOIN word AS w1 ON w1.id = t.original_word_id " +
            "  JOIN word AS w2 ON w2.id = t.translated_word_id " +
            "  JOIN language AS l ON l.id = w2.language_id " +
            "WHERE w1.language_level_id = w2.language_level_id " +
            "ORDER BY l.name")
    LiveData<List<String>> getAvailableLearningLanguages();

    @Query("SELECT DISTINCT l.name " +
            "FROM translation AS t " +
            "  JOIN word AS w1 ON w1.id = t.original_word_id " +
            "  JOIN word AS w2 ON w2.id = t.translated_word_id " +
            "  JOIN language AS l ON l.id = w1.language_id " +
            "WHERE w1.language_level_id = w2.language_level_id " +
            "ORDER BY l.name")
    LiveData<List<String>> getAvailableNativeLanguages();

    @Query("SELECT DISTINCT lvl.name " +
            "FROM translation AS t " +
            "  JOIN word AS w1 ON w1.id = t.original_word_id " +
            "  JOIN word AS w2 ON w2.id = t.translated_word_id " +
            "  JOIN language_level AS lvl ON lvl.id = w2.language_level_id " +
            "WHERE w1.language_level_id = w2.language_level_id " +
            "ORDER BY lvl.id")
    LiveData<List<String>> getAvailableLanguageLevels();

    @Query("SELECT * FROM translation AS t WHERE t.id IN (:translationIds)")
    List<Translation> getTranslationsByIds(long[] translationIds);
}