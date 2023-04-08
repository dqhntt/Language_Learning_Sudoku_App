package ca.sfu.cmpt276.sudokulang.data;

import androidx.room.ColumnInfo;

public class BoardDimension {
    @ColumnInfo(name = "size")
    private final int boardSize;

    @ColumnInfo(name = "subgrid_height")
    private final int subgridHeight;

    @ColumnInfo(name = "subgrid_width")
    private final int subgridWidth;

    public BoardDimension(int boardSize, int subgridHeight, int subgridWidth) {
        this.boardSize = boardSize;
        this.subgridHeight = subgridHeight;
        this.subgridWidth = subgridWidth;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getSubgridHeight() {
        return subgridHeight;
    }

    public int getSubgridWidth() {
        return subgridWidth;
    }
}