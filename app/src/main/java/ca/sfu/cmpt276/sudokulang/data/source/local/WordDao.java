package ca.sfu.cmpt276.sudokulang.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Word;

@Dao
public interface WordDao {
    @Query("SELECT id FROM word WHERE text = :word")
    long getIdOfWord(String word);

    @Insert
    long insert(Word word);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long[] insert(List<Word> words);
}