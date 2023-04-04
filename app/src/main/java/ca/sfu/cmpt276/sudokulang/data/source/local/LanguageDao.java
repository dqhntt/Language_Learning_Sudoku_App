package ca.sfu.cmpt276.sudokulang.data.source.local;

import androidx.room.Dao;
import androidx.room.Query;

import ca.sfu.cmpt276.sudokulang.data.Language;

@Dao
public interface LanguageDao {
    @Query("SELECT * FROM language WHERE name = :languageName")
    Language getLanguageByName(String languageName);
}