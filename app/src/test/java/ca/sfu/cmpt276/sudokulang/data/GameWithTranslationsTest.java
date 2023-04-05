package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class GameWithTranslationsTest {
    @Test
    void constructorTest() {
        final var obj = new GameWithTranslations();
        assertNull(obj.mGame);
        assertNull(obj.mTranslations);
    }
}