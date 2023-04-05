package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Locale;

class ConvertersTest {
    private final Converters c = new Converters();

    @Test
    void localeFromString() {
        assertEquals(new Locale("fr"), Converters.localeFromString("fr"));
    }

    @Test
    void localeToString() {
        assertEquals("fr", Converters.localeToString(new Locale("fr")));
    }

    @Test
    void dateFromTimestamp() {
        final long time = 123456789;
        assertEquals(new Date(time), Converters.dateFromTimestamp(time));
    }

    @Test
    void dateToTimestamp() {
        final var time = new Date(123456789);
        assertEquals(123456789, Converters.dateToTimestamp(time));
    }

    @Test
    void cellsFromString() {
        assertNull(Converters.cellsFromString(null));
        assertNull(Converters.cellsFromString(" "));

        final var cells = Converters.cellsFromString("0,1,2,0");
        assertEquals(0, cells[0][0].getValue());
        assertEquals(1, cells[0][1].getValue());
        assertEquals(2, cells[1][0].getValue());
        assertEquals(0, cells[1][1].getValue());

        assertThrows(IllegalArgumentException.class, () -> Converters.cellsFromString("0,1,2"));
    }

    @Test
    void cellsToString() {
        assertNull(Converters.cellsToString(null));

        final var cells = new CellImpl[2][2];
        cells[0][0] = new CellImpl().setValue(2).setPrefilled(true);
        cells[0][1] = new CellImpl().setValue(0);
        cells[1][0] = new CellImpl().setValue(1).setErrorCell(true);
        cells[1][1] = new CellImpl().setValue(2);
        assertEquals("2,+0,-1,+2", Converters.cellsToString(cells));
    }
}