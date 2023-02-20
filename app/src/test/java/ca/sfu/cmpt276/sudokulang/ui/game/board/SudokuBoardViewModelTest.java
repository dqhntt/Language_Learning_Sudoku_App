package ca.sfu.cmpt276.sudokulang.ui.game.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ca.sfu.cmpt276.sudokulang.ui.InstantExecutorExtension;

@ExtendWith(InstantExecutorExtension.class)
class SudokuBoardViewModelTest {
    private SudokuBoardViewModel board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoardViewModel();
    }

    @Test
    void generateNewBoard() {
        board.generateNewBoard(9, 3, 3);
        assertEquals(9, board.getBoardSize());
        assertEquals(3, board.getSubgridHeight());
        assertEquals(3, board.getSubgridWidth());
        assertThrows(IllegalArgumentException.class, () -> board.generateNewBoard(9, 2, 5));
    }

    @Test
    void getSelectedCell() {
        board.generateNewBoard(4, 2, 2);
        board.setSelectedCell(0, 2);
        var selectedCell = board.getSelectedCell().getValue();
        assertEquals(0, selectedCell.getRowIndex().getValue());
        assertEquals(2, selectedCell.getColIndex().getValue());
    }

    @Test
    void setSelectedCell() {
        board.generateNewBoard(9, 3, 3);
        board.setSelectedCell(0, 3);
        var selectedCell = board.getSelectedCell().getValue();
        assertEquals(0, selectedCell.getRowIndex().getValue());
        assertEquals(3, selectedCell.getColIndex().getValue());

    }

    @Test
    void setNoSelectedCell() {
        board.generateNewBoard(4, 2, 2);
        board.setSelectedCell(0, 2);
        board.setNoSelectedCell();
        assertNull(board.getSelectedCell().getValue());
    }

    @Test
    void updateNumEmptyCells() {
        board.generateNewBoard(4, 2, 2);
        assertEquals(16, board.getNumEmptyCells().getValue());
        board.setSelectedCell(1, 3);
        var selectedCell = board.getSelectedCell().getValue();
        selectedCell.setText("Non-empty");
        board.updateNumEmptyCells();
        assertEquals(15, board.getNumEmptyCells().getValue());
    }

    @Test
    void getNumEmptyCells() {
        // NOTE: updateNumEmptyCells() must me called right before getNumEmptyCells() for it to work correctly.
        board.generateNewBoard(4, 2, 2);
        assertEquals(16, board.getNumEmptyCells().getValue());
        board.setSelectedCell(3, 1);
        var selectedCell = board.getSelectedCell().getValue();
        selectedCell.setText("Null");
        board.updateNumEmptyCells();
        assertEquals(15, board.getNumEmptyCells().getValue());
    }

    @Test
    void getBoardDimension() {
        board.generateNewBoard(12, 3, 4);
        final var dimension = board.getBoardDimension().getValue();
        assertEquals(12, dimension.boardSize);
        assertEquals(3, dimension.subgridHeight);
        assertEquals(4, dimension.subgridWidth);
        assertEquals(board.getBoardSize(), dimension.boardSize);
        assertEquals(board.getSubgridHeight(), dimension.subgridHeight);
        assertEquals(board.getSubgridWidth(), dimension.subgridWidth);
    }

    @Test
    void getBoardSize() {
        board.generateNewBoard(4, 2, 2);
        assertEquals(4, board.getBoardSize());
    }

    @Test
    void getSubgridHeight() {
        board.generateNewBoard(4, 1, 4);
        assertEquals(1, board.getSubgridHeight());
    }

    @Test
    void getSubgridWidth() {
        board.generateNewBoard(4, 1, 4);
        assertEquals(4, board.getSubgridWidth());
    }

    @Test
    void isValidBoard() {
        board.generateNewBoard(4, 2, 2);
        assertEquals(16, board.getNumEmptyCells().getValue());
        board.setSelectedCell(3, 1);
        var selectedCell = board.getSelectedCell().getValue();
        assertTrue(board.isValidBoard());
        selectedCell.setAsErrorCell(true);
        assertFalse(board.isValidBoard());
    }

    @Test
    void getCells() {
        final var cells = board.getCells();
        final var boardSize = board.getBoardSize();
        assertEquals(boardSize, cells.length);
        assertEquals(boardSize, cells[0].length);
    }

    @Test
    void getDataValuePairs() {
        assertEquals(board.getBoardSize(), board.getDataValuePairs().length);
    }

//    @Test
//    void isValidValueForCell() {
//        board.generateNewBoard(9, 3, 3);
//        board.setSelectedCell(5, 5);
//        var selectedCell = board.getSelectedCell().getValue();
//        selectedCell.setText("Br");
//        assertTrue(board.isValidValueForCell("Ca", 5, 4));
//        assertFalse(board.isValidValueForCell("Br", 7, 5));
//        assertFalse(board.isValidValueForCell("Br", 5, 0));
//        assertFalse(board.isValidValueForCell("Br", 3, 3));
//
//        board.setSelectedCell(4, 5);
//        selectedCell = board.getSelectedCell().getValue();
//        assertFalse(board.isValidValueForCell("Br", selectedCell));
//
//        board.generateNewBoard(4, 4, 4);
//        board.setSelectedCell(0, 0);
//        selectedCell = board.getSelectedCell().getValue();
//        selectedCell.setText("Kr");
//        assertTrue(board.isValidValueForCell("Ca", 0, 3));
//        assertFalse(board.isValidValueForCell("Kr", 3, 0));
//    }
}