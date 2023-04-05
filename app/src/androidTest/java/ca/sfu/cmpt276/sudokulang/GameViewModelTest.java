package ca.sfu.cmpt276.sudokulang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GameViewModelTest {
    private final GameViewModel game = new GameViewModel(ApplicationProvider.getApplicationContext());

    @Test
    public void getBoardUiState() {
        assertNull(game.getBoardUiState().getValue());
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        assertNotNull(game.getBoardUiState().getValue());
    }

    @Test
    public void startNewGame() {
        game.startNewGame("English", "French", "Beginner", "Novice", 9, 3, 3, false);
        final var board = game.getBoardUiState().getValue();
        assertNotNull(board);
        assertEquals(9, board.getBoardSize());
        assertEquals(3, board.getSubgridHeight());
        assertEquals(3, board.getSubgridWidth());
        assertEquals(-1, board.getSelectedRowIndex());
        assertEquals(-1, board.getSelectedColIndex());
        assertNull(board.getSelectedCell());
        assertNotNull(board.getCells());
        assertFalse(board.isSolvedBoard());
        assertThrows(IllegalArgumentException.class, () -> game.startNewGame("English", "French", "Beginner", "Novice", 9, 2, 5, false));
    }

    @Test
    public void isGameInProgress() {
        assertFalse(game.isGameInProgress().getValue());
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        assertTrue(game.isGameInProgress().getValue());
    }

    @Test
    public void endGame() {
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        assertTrue(game.isGameInProgress().getValue());
        game.endGame();
        assertFalse(game.isGameInProgress().getValue());
    }

    @Test
    public void pauseGame() {
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        assertTrue(game.isGameInProgress().getValue());
        game.pauseGame();
        assertFalse(game.isGameInProgress().getValue());
    }

    @Test
    public void resumeGame() {
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        assertTrue(game.isGameInProgress().getValue());
        game.pauseGame();
        assertFalse(game.isGameInProgress().getValue());
        game.resumeGame();
        assertTrue(game.isGameInProgress().getValue());
    }

    @Test
    public void resetGame() {
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        assertEquals(4, game.getBoardUiState().getValue().getBoardSize());
        assertEquals(4, game.getBoardUiState().getValue().getSubgridHeight());
        assertEquals(4, game.getBoardUiState().getValue().getSubgridWidth());
        game.resetGame();
        assertEquals(4, game.getBoardUiState().getValue().getBoardSize());
        assertEquals(4, game.getBoardUiState().getValue().getSubgridHeight());
        assertEquals(4, game.getBoardUiState().getValue().getSubgridWidth());
    }

    @Test
    public void setSelectedCell() {
        game.startNewGame("English", "French", "Beginner", "Novice", 9, 3, 3, false);
        game.setSelectedCell(0, 3);
        final var selectedCell = game.getBoardUiState().getValue().getSelectedCell();
        assertNotNull(selectedCell);
        final var cells = game.getBoardUiState().getValue().getCells();
        assertNotNull(cells);
        assertEquals(cells[0][3], selectedCell);

        game.startNewGame("English", "French", "Beginner", "Novice", 12, 3, 4, false);
        assertThrows(IllegalArgumentException.class, () -> game.setSelectedCell(5, 15));
        assertEquals(cells[0][3], selectedCell);
    }

    @Test
    public void setNoSelectedCell() {
        game.startNewGame("English", "French", "Beginner", "Novice", 6, 2, 3, false);
        game.setSelectedCell(2, 0);
        assertNotNull(game.getBoardUiState().getValue().getSelectedCell());
        game.setNoSelectedCell();
        assertNull(game.getBoardUiState().getValue().getSelectedCell());
    }

    @Test
    public void setSelectedCellText() {
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        game.setSelectedCellText("Null");
        assertNull(game.getBoardUiState().getValue().getSelectedCell());
        game.setSelectedCell(3, 3);
        var selectedCell = game.getBoardUiState().getValue().getSelectedCell();
        final var text = selectedCell.getText();
        final var prefilled = selectedCell.isPrefilled();
        game.setSelectedCellText("Non-empty");
        selectedCell = game.getBoardUiState().getValue().getSelectedCell();
        if (!prefilled) {
            assertEquals("Non-empty", selectedCell.getText());
        } else {
            assertEquals(text, selectedCell.getText());
        }
        assertEquals(prefilled, selectedCell.isPrefilled());
    }

    @Test
    public void clearSelectedCell() {
        game.startNewGame("English", "French", "Beginner", "Novice", 6, 3, 6, false);
        game.clearSelectedCell();
        assertNull(game.getBoardUiState().getValue().getSelectedCell());
        game.setSelectedCell(0, 0);
        final var selectedCell = game.getBoardUiState().getValue().getSelectedCell();
        final var text = selectedCell.getText();
        final var prefilled = selectedCell.isPrefilled();
        game.clearSelectedCell();
        if (!prefilled) {
            assertEquals("", selectedCell.getText());
        } else {
            assertEquals(text, selectedCell.getText());
        }
        assertEquals(prefilled, selectedCell.isPrefilled());
        assertFalse(selectedCell.isErrorCell());
    }

    @Test
    public void getWordPairs() {
        game.startNewGame("English", "French", "Beginner", "Novice", 9, 3, 3, false);
        assertEquals(
                game.getBoardUiState().getValue().getBoardSize(),
                game.getWordPairs().getValue().length
        );
    }

    @Test
    public void isComprehensionMode() {
        game.startNewGame("English", "French", "Beginner", "Novice", 9, 3, 3, false);
        assertFalse(game.isComprehensionMode());
        game.startNewGame("English", "French", "Beginner", "Novice", 9, 3, 3, true);
        assertTrue(game.isComprehensionMode());
    }

    @Test
    public void getLearningLang() {
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        assertEquals("French", game.getLearningLang().getName());
    }

    @Test
    public void getNativeLang() {
        game.startNewGame("English", "French", "Beginner", "Novice", 4, 4, 4, false);
        assertEquals("English", game.getNativeLang().getName());
    }
}