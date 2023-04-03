//package ca.sfu.cmpt276.sudokulang.ui.game;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import android.app.Application;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import ca.sfu.cmpt276.sudokulang.GameViewModel;
//import ca.sfu.cmpt276.sudokulang.ui.InstantExecutorExtension;
//
//@ExtendWith(MockitoExtension.class)
//@ExtendWith(InstantExecutorExtension.class)
//class GameViewModelTest {
//    private GameViewModel game;
//
//    @BeforeEach
//    void setUp() {
//        game = new GameViewModel(Mockito.mock(Application.class));
//    }
//
//    @Test
//    void getBoardUiState() {
//        assertNull(game.getBoardUiState().getValue());
//        game.generateNewBoard(4, 4, 4);
//        assertNotNull(game.getBoardUiState().getValue());
//    }
//
//    @Test
//    void generateNewBoard() {
//        game.generateNewBoard(9, 3, 3);
//        final var board = game.getBoardUiState().getValue();
//        assertNotNull(board);
//        assertEquals(9, board.getBoardSize());
//        assertEquals(3, board.getSubgridHeight());
//        assertEquals(3, board.getSubgridWidth());
//        assertEquals(-1, board.getSelectedRowIndex());
//        assertEquals(-1, board.getSelectedColIndex());
//        assertNull(board.getSelectedCell());
//        assertNotNull(board.getCells());
//        assertFalse(board.isSolvedBoard());
//        assertThrows(IllegalArgumentException.class, () -> game.generateNewBoard(9, 2, 5));
//    }
//
//    @Test
//    void setSelectedCell() {
//        game.generateNewBoard(9, 3, 3);
//        game.setSelectedCell(0, 3);
//        final var selectedCell = game.getBoardUiState().getValue().getSelectedCell();
//        assertNotNull(selectedCell);
//        final var cells = game.getBoardUiState().getValue().getCells();
//        assertNotNull(cells);
//        assertEquals(cells[0][3], selectedCell);
//
//        game.generateNewBoard(12, 3, 4);
//        game.setSelectedCell(5, 15);
//        assertNull(game.getBoardUiState().getValue().getSelectedCell());
//        assertNotNull(game.getBoardUiState().getValue().getCells());
//    }
//
//    @Test
//    void setNoSelectedCell() {
//        game.generateNewBoard(6, 2, 3);
//        game.setSelectedCell(2, 0);
//        assertNotNull(game.getBoardUiState().getValue().getSelectedCell());
//        game.setNoSelectedCell();
//        assertNull(game.getBoardUiState().getValue().getSelectedCell());
//    }
//
//    @Test
//    void setSelectedCellText() {
//        game.generateNewBoard(4, 4, 4);
//        game.setSelectedCellText("Null");
//        assertNull(game.getBoardUiState().getValue().getSelectedCell());
//        game.setSelectedCell(3, 3);
//        var selectedCell = game.getBoardUiState().getValue().getSelectedCell();
//        final var text = selectedCell.getText();
//        final var prefilled = selectedCell.isPrefilled();
//        game.setSelectedCellText("Non-empty");
//        selectedCell = game.getBoardUiState().getValue().getSelectedCell();
//        if (!prefilled) {
//            assertEquals("Non-empty", selectedCell.getText());
//        } else {
//            assertEquals(text, selectedCell.getText());
//        }
//        assertEquals(prefilled, selectedCell.isPrefilled());
//    }
//
//    @Test
//    void setSelectedCellText2() {
//        // For temporary testing of the board data.
//        game.generateNewBoard(9, 3, 3);
//        game.setSelectedCell(5, 5);
//        game.setSelectedCellText("Cr");
//        assertFalse(game.getBoardUiState().getValue().getSelectedCell().isErrorCell());
//        game.setSelectedCell(5, 0);
//        game.setSelectedCellText("Fe");
//        assertTrue(game.getBoardUiState().getValue().getSelectedCell().isErrorCell());
//        game.setSelectedCell(8, 5);
//        game.setSelectedCellText("Cr");
//        assertTrue(game.getBoardUiState().getValue().getSelectedCell().isErrorCell());
//        game.setSelectedCell(4, 3);
//        game.setSelectedCellText("Cr");
//        assertTrue(game.getBoardUiState().getValue().getSelectedCell().isErrorCell());
//    }
//
//    @Test
//    void clearSelectedCell() {
//        game.generateNewBoard(6, 3, 6);
//        game.clearSelectedCell();
//        assertNull(game.getBoardUiState().getValue().getSelectedCell());
//        game.setSelectedCell(0, 0);
//        final var selectedCell = game.getBoardUiState().getValue().getSelectedCell();
//        final var text = selectedCell.getText();
//        final var prefilled = selectedCell.isPrefilled();
//        game.clearSelectedCell();
//        if (!prefilled) {
//            assertEquals("", selectedCell.getText());
//        } else {
//            assertEquals(text, selectedCell.getText());
//        }
//        assertEquals(prefilled, selectedCell.isPrefilled());
//        assertFalse(selectedCell.isErrorCell());
//    }
//
//    @Test
//    void getDataValuePairs() {
//        game.generateNewBoard(9, 3, 3);
//        assertEquals(
//                game.getBoardUiState().getValue().getBoardSize(),
//                game.getWordPairs().length
//        );
//    }
//}