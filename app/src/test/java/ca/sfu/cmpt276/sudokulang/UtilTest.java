package ca.sfu.cmpt276.sudokulang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class UtilTest {
    private final Util util = new Util();

    @Test
    void formatWithTime() {
        final long timeInMs = 1112000; // 18m 32s
        assertEquals("18:32", Util.formatWithTime("%02d:%02d", timeInMs));
        assertEquals("18m 32s", Util.formatWithTime("%02dm %02ds", timeInMs));
        assertEquals("18'32\"", Util.formatWithTime("%02d'%02d\"", timeInMs));
    }

    @Test
    void isUnsignedInteger() {
        for (var n : TestData.INTEGERS) {
            assertEquals(n >= 0, Util.isUnsignedInteger(String.valueOf(n)));
        }
        assertFalse(Util.isUnsignedInteger(""));
        assertFalse(Util.isUnsignedInteger("    "));
        assertFalse(Util.isUnsignedInteger("not integer 123"));
        assertFalse(Util.isUnsignedInteger("321 hahaha"));
        assertFalse(Util.isUnsignedInteger("123 321"));
        assertFalse(Util.isUnsignedInteger("n1"));
        assertFalse(Util.isUnsignedInteger("2m"));
    }
}