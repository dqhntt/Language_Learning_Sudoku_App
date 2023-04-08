package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CellImplTest {
    @Test
    void getValue() {
        CellImpl cell = new CellImpl();
        assertEquals(0, cell.getValue());
    }

    @Test
    void setValue() {
        CellImpl cell = new CellImpl();

        assertThrows(IllegalArgumentException.class, () -> cell.setValue(-1));
    }

    @Test
    void getText() {
        CellImpl cell = new CellImpl();
        assertEquals("", cell.getText());
    }

    @Test
    void setText() {
        CellImpl cell = new CellImpl();
        cell.setText("Test");
        assertEquals("Test", cell.getText());
    }

    @Test
    void isPrefilled() {
        CellImpl cell = new CellImpl();
        assertFalse(cell.isPrefilled());
    }

    @Test
    void setPrefilled() {
        CellImpl cell = new CellImpl();
        cell.setPrefilled(true);
        assertThrows(AssertionError.class, cell::isPrefilled);
        assertThrows(IllegalArgumentException.class, () -> cell.setErrorCell(true));
    }

    @Test
    void isErrorCell() {
        CellImpl cell = new CellImpl();
        assertFalse(cell.isErrorCell());
    }

    @Test
    void setErrorCell() {
        CellImpl cell = new CellImpl();
        cell.setErrorCell(true);
        assertThrows(AssertionError.class, cell::isErrorCell);
        assertThrows(IllegalArgumentException.class, () -> cell.setPrefilled(true));
    }

    @Test
    void isEmpty() {
        assertTrue(new CellImpl().isEmpty());
    }
}