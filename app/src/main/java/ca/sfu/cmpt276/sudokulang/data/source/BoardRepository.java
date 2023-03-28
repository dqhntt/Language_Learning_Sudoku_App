package ca.sfu.cmpt276.sudokulang.data.source;

import androidx.lifecycle.LiveData;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.BoardDimension;
import ca.sfu.cmpt276.sudokulang.data.BoardImpl;

public interface BoardRepository {
    BoardImpl getBoardById(long id);

    LiveData<List<BoardDimension>> getAvailableBoardDimensions();

    LiveData<List<BoardDimension>> getAvailableBoardDimensionsByLevel(String level);

    LiveData<List<String>> getAvailableBoardLevels();

    LiveData<List<String>> getAvailableBoardLevelsByDimension(int boardSize, int subgridHeight, int subgridWidth);

    BoardImpl getARandomBoardMatching(int boardSize, int subgridHeight, int subgridWidth, String level);
}