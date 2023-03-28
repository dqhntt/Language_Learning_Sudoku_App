package ca.sfu.cmpt276.sudokulang.data.source.local;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface LanguageDao {
    @Query("SELECT id FROM language WHERE name = :language")
    long getIdOfLanguage(String language);

    @Query("SELECT id FROM language_level WHERE name = :languageLevel")
    long getIdOfLanguageLevel(String languageLevel);
}