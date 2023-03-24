package ca.sfu.cmpt276.sudokulang.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.BoardDimension;
import ca.sfu.cmpt276.sudokulang.data.BoardImpl;

@Dao
public interface BoardDao {
    @Query("SELECT size, subgrid_height, subgrid_width " +
            "FROM board " +
            "GROUP BY size, subgrid_height, subgrid_width " +
            "ORDER BY size")
    LiveData<List<BoardDimension>> getAvailableBoardDimensions();

    @Query("SELECT size, subgrid_height, subgrid_width " +
            "FROM board " +
            "WHERE level = :level " +
            "GROUP BY size, subgrid_height, subgrid_width " +
            "ORDER BY size")
    LiveData<List<BoardDimension>> getAvailableBoardDimensionsByLevel(String level);

    @Query("SELECT DISTINCT b.level " +
            "FROM board AS b " +
            "ORDER BY b.level DESC")
    LiveData<List<String>> getAvailableBoardLevels();

    @Query("SELECT DISTINCT level " +
            "FROM board " +
            "WHERE size = :boardSize " +
            "  AND subgrid_height = :subgridHeight " +
            "  AND subgrid_width = :subgridWidth " +
            "ORDER BY level DESC")
    LiveData<List<String>> getAvailableBoardLevelsByDimension(int boardSize, int subgridHeight, int subgridWidth);

    @Query("SELECT * FROM board WHERE id = :id")
    BoardImpl getBoardById(long id);

    @Query("SELECT * " +
            "FROM board " +
            "WHERE size = :boardSize " +
            "  AND subgrid_height = :subgridHeight " +
            "  AND subgrid_width = :subgridWidth " +
            "  AND level = :level")
    List<BoardImpl> getAllFilteredBoards(int boardSize, int subgridHeight, int subgridWidth, String level);
}