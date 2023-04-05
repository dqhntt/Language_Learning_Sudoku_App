package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class GameTranslationTest {
    private GameTranslation gameTrans;

    @BeforeEach
    void setup() {
        gameTrans = new GameTranslation(36, 49);
    }

    @Test
    void getGameId() {
        assertEquals(36, gameTrans.getGameId());
    }

    @Test
    void getTranslationId() {
        assertEquals(49, gameTrans.getTranslationId());
    }
}