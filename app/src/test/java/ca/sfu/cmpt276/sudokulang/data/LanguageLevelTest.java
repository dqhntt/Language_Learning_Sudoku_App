package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class LanguageLevelTest {
    private LanguageLevel level;

    @BeforeEach
    void setup() {
        level = new LanguageLevel(6, "Beginner");
    }

    @Test
    void getId() {
        assertEquals(6, level.getId());
    }

    @Test
    void getName() {
        assertEquals("Beginner", level.getName());
    }
}