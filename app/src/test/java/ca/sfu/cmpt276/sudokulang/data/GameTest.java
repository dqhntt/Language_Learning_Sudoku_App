package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

    @BeforeEach
    void setGame() {

        //currentBoard[][] =

        game = new Game(1, 2022-04-03, 12, false, 3, currentBoard);
    }

    @Test
    void getId() {
        assertEquals(1, game.getId());
    }
    //

    @Test
    void setId() {
        game.setId(0);
        assertEquals(0, game.getId());
    }

    @Test
    void getStartTime() {
        assertEquals(2022-04-03, game.getStartTime());
    }

    @Test
    void setStartTime() {
        game.setStartTime(2023-01-02);
        assertEquals(2023-01-02, game.getStartTime());
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
        assertEquals(false, game.isCompleted());
    }

    @Test
    void setCompleted() {
        game.setCompleted(true);
        assertEquals(true, game.setCompleted);
    }

    @Test
    void getBoardId() {
        assertEquals(3, game.getBoardId);
    }

    @Test
    void getCurrentBoardValues() {
        //assertEquals();
    }

    @Test
    void setCurrentBoardValues() {
    }
}