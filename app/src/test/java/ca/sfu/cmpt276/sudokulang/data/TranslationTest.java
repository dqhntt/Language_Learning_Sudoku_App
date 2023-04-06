package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TranslationTest {
    private Translation trans, trans2;

    @BeforeEach
    void setup() {
        trans = new Translation(1, 0, 2, false);
        trans2 = new Translation(3, 4, true);
    }

    @Test
    void getId() {
        assertEquals(1, trans.getId());
    }

    @Test
    void getOriginalWordId() {
        assertEquals(0, trans.getOriginalWordId());
        assertEquals(3, trans2.getOriginalWordId());
    }

    @Test
    void getTranslatedWordId() {
        assertEquals(2, trans.getTranslatedWordId());
        assertEquals(4, trans2.getTranslatedWordId());
    }

    @Test
    void isFavourite() {
        assertFalse(trans.isFavourite());
        assertTrue(trans2.isFavourite());
    }
}