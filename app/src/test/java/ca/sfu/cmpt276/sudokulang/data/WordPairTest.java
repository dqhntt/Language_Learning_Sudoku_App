package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class WordPairTest {
    private WordPair pair;

    @BeforeEach
    void setup() {
        pair = new WordPair(1, "Hello", "Bonjor");
    }

    @Test
    void getTranslationId() {
        assertEquals(1, pair.getTranslationId());
    }

    @Test
    void getOriginalWord() {
        assertEquals("Hello", pair.getOriginalWord());
    }

    @Test
    void getTranslatedWord() {
        assertEquals("Bonjor", pair.getTranslatedWord());
    }
}