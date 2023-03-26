package ca.sfu.cmpt276.sudokulang.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.GameTranslation;
import ca.sfu.cmpt276.sudokulang.data.GameWithTranslations;

@Dao
public interface GameTranslationDao {
    @Transaction
    @Query("SELECT * FROM game")
    List<GameWithTranslations> getAllGamesWithTranslations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameTranslation gameTranslation);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insert(List<GameTranslation> gameTranslations);
}