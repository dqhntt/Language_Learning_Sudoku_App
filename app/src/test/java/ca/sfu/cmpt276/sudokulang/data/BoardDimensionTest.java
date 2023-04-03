package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardDimensionTest {

    @Test
    void getBoardSize() {
        final int size = 1;
        BoardDimension bd1 = new BoardDimension(size,2,3);
        assertEquals(size, bd1.getBoardSize());
    }

    @Test
    void getSubgridHeight() {
        final int subgrid_height = 3;
        BoardDimension bd2 = new BoardDimension(1, subgrid_height, 2);
        assertEquals(subgrid_height, bd2.getSubgridHeight());
    }

    @Test
    void getSubgridWidth() {
        final int subgrid_width = 4;
        BoardDimension bd3 = new BoardDimension(2, 3, subgrid_width);
        assertEquals(subgrid_width, bd3.getSubgridWidth());
    }
}