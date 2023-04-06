package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WordTest {
    private Word word, word2;

    @BeforeEach
    void setup() {
        word = new Word(4, "Hello", 1, 0);
        word2 = new Word("Bonjor", 2, 3);
    }

    @Test
    void getId() {
        assertEquals(4, word.getId());
    }

    @Test
    void getText() {
        assertEquals("Hello", word.getText());
        assertEquals("Bonjor", word2.getText());
    }

    @Test
    void getLanguageId() {
        assertEquals(1, word.getLanguageId());
        assertEquals(2, word2.getLanguageId());
    }

    @Test
    void getLanguageLevelId() {
        assertEquals(0, word.getLanguageLevelId());
        assertEquals(3, word2.getLanguageLevelId());
    }
}