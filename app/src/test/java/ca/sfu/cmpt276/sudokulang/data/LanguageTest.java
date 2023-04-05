package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class LanguageTest {
    private Language lang;

    @BeforeEach
    void setup() {
        lang = new Language(27, "English", Locale.ENGLISH);
    }

    @Test
    void getId() {
        assertEquals(27, lang.getId());
    }

    @Test
    void getName() {
        assertEquals("English", lang.getName());
    }

    @Test
    void getCode() {
        assertEquals(Locale.ENGLISH, lang.getCode());
    }
}