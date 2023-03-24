package ca.sfu.cmpt276.sudokulang.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import ca.sfu.cmpt276.sudokulang.data.Game;

@Dao
public interface GameDao {
    @Insert
    long insert(Game game);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Game game);
}