package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

class GameTest {
    private Game game, game2;
    CellImpl[][] currentBoard;

    @BeforeEach
    void setGame() {
        currentBoard = new CellImpl[2][2];

        currentBoard[0][0] = new CellImpl().setValue(1);
        currentBoard[0][1] = new CellImpl().setValue(3);
        currentBoard[1][0] = new CellImpl().setValue(4);
        currentBoard[1][1] = new CellImpl().setValue(2);

        game = new Game(1, new Date(123456789), 12, false, 3, currentBoard);
        game2 = new Game(5, currentBoard);
    }

    @Test
    void getId() {
        assertEquals(1, game.getId());
    }

    @Test
    void setId() {
        game.setId(0);
        assertEquals(0, game.getId());
    }

    @Test
    void getStartTime() {
        assertEquals(new Date(123456789), game.getStartTime());
    }

    @Test
    void setStartTime() {
        game.setStartTime(new Date(987654321));
        assertEquals(new Date(987654321), game.getStartTime());
    }

    @Test
    void getTimeDuration() {
        assertEquals(12, game.getTimeDuration());
    }

    @Test
    void setTimeDuration() {
        game.setTimeDuration(10);
        assertEquals(10, game.getTimeDuration());
    }

    @Test
    void isCompleted() {
        assertFalse(game.isCompleted());
    }

    @Test
    void setCompleted() {
        game.setCompleted(true);
        assertTrue(game.isCompleted());
    }

    @Test
    void getBoardId() {
        assertEquals(3, game.getBoardId());
        assertEquals(5, game2.getBoardId());
    }

    @Test
    void setCurrentBoardValues() {
        game.setCurrentBoardValues(currentBoard);
        final var cells = game.getCurrentBoardValues();
        for (int i = 0; i < cells.length;i++){
            for (int j = 0; j <cells[0].length;j++){
                assertEquals(currentBoard[i][j].getValue() ,cells[i][j].getValue());
            }
        }
    }
}