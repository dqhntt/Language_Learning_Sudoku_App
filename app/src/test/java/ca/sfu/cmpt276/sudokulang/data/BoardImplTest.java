package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardImplTest {
    //generate board
    BoardImpl board;

    @BeforeEach // before running those test

    void setup(){
        CellImpl[][] prefilled = new CellImpl[2][2]; //create a 2-by-2 board

        //set values, 0 means empty, 1
        prefilled[0][0] = new CellImpl().setValue(1);
        prefilled[0][1] = new CellImpl().setValue(0);
        prefilled[1][0] = new CellImpl().setValue(0);
        prefilled[1][1] = new CellImpl().setValue(2);

        CellImpl[][] solved = new CellImpl[2][2];

        //finished
        solved[0][0] = new CellImpl().setValue(1);
        solved[0][1] = new CellImpl().setValue(4);
        solved[1][0] = new CellImpl().setValue(3);
        solved[1][1] = new CellImpl().setValue(2);

        //create
        board = new BoardImpl(0,"d",2,2,2,prefilled,solved);
    }

    @Test
    void resetBoard() {
        Cell[][] currentCells = board.getCells(); //set currentCells as prefilled cell

        CellImpl currentCell = (CellImpl) currentCells[0][1];//@ point [0][1]
        currentCell.setValue(3);//set value = 3
        assertEquals(3, currentCells[0][1].getValue());// test if user input successfully set to the board

        board.resetBoard();
        currentCells = board.getCells();
        assertEquals(0, currentCells[0][1].getValue());
    }

    @Test
    void getBoardSize() {

        assertEquals(2, board.getBoardSize());
    }

    @Test
    void getSubgridHeight() {
        assertEquals(2, board.getSubgridHeight());
    }

    @Test
    void getSubgridWidth() {
        assertEquals(2, board.getSubgridWidth());
    }

    @Test// move to cellImpl
    void getSelectedRowIndex() {
//        Cell[][] currentCells = board.getCells(); //set currentCells as prefilled cell
//
//        CellImpl currentCell = (CellImpl) currentCells[1][0];//@ point [0][1]
        board.setSelectedIndexes(1, 0);
        assertEquals(1, board.getSelectedRowIndex());
    }




    @Test
    void getSelectedColIndex() {
        Cell[][] currentCells = board.getCells(); //set currentCells as prefilled cell

        CellImpl currentCell = (CellImpl) currentCells[0][1];

        board.setSelectedIndexes(0, 0);
        assertEquals(1, board.getSelectedColIndex());

    }

    @Test
    void setSelectedIndexes() {
        BoardImpl board1 = new BoardImpl();
        board1.setSelectedIndexes(-1, -1);
        assertEquals(null, board1.getSelectedCell());
    }

    @Test
    void getSelectedCell() {
        Cell[][] cells = board.getCells();

        // select a cell at row 1, column 0
        board.setSelectedIndexes(1, 0);

        // assert that the selected cell is the same as the cell at row 1, column 0
        assertEquals(cells[1][0], board.getSelectedCell());

    }

    @Test
    void getCells() {
        Cell[][] cells = board.getCells();
        assertEquals(cells, board.getCells());
    }

    @Test
    void isSolvedBoard() {
        boolean expected = false; // or true, depending on what you expect the return value to be
        boolean actual = board.isSolvedBoard();
        assertEquals(expected, actual);
    }

    @Test
    void getId() {
        assertEquals(0, board.getId());
    }

    @Test
    void getDifficultyLevel() {
        assertEquals("d", board.getDifficultyLevel());
    }

    @Test
    void getPrefilledValues() {
        assertEquals(2, board.getPrefilledValues());

    }

    @Test
    void getSolvedValues() {
        assertEquals(2, board.getSolvedValues());
    }
}