package ca.sfu.cmpt276.sudokulang.ui.game.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(InstantExecutorExtension.class)
class SudokuBoardViewModelTest {
    private SudokuBoardViewModel board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoardViewModel();
    }

    @Test
    void createEmptyBoard() {
        board.createEmptyBoard(9, 3, 3);
        assertEquals(9, board.getBoardSize().getValue());
    }

    @Test
    void getSelectedCell() {
        board.createEmptyBoard(4, 2, 2);
        board.setSelectedCell(0, 2);
        var selectedCell = board.getSelectedCell().getValue();
        assertEquals(0, selectedCell.getRowIndex().getValue());
        assertEquals(2, selectedCell.getColIndex().getValue());
    }

    @Test
    void setSelectedCell() {
    }

    @Test
    void setNoSelectedCell() {
    }

    @Test
    void updateNumEmptyCells() {
    }

    @Test
    void getNumEmptyCells() {
        // NOTE: updateNumEmptyCells() must me called right before getNumEmptyCells() for it to work correctly.
    }

    @Test
    void getBoardSize() {
    }

    @Test
    void getSubgridHeight() {
    }

    @Test
    void getSubgridWidth() {
    }

    @Test
    void isValidBoard() {
    }

    @Test
    void isValidValueForCell() {
    }
}