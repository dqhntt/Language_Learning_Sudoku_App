package ca.sfu.cmpt276.sudokulang.data;

import static org.junit.jupiter.api.Assertions.*;

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

        assertThrows(IllegalArgumentException.class, () -> {
            cell.setValue(-1);
        });
    }

    @Test
    void getText() {
        CellImpl cell = new CellImpl();
        assertEquals("", cell.getText());
    }

    @Test
    void setText() {
        CellImpl cell = new CellImpl();
        //?
    }

    @Test
    void isPrefilled() {
        CellImpl cell = new CellImpl();
        assertEquals(false, cell.isPrefilled());
    }

    @Test
    void setPrefilled() {
    }

    @Test
    void isErrorCell() {
        CellImpl cell = new CellImpl();
        assertEquals(false, cell.isErrorCell());
    }

    @Test
    void setErrorCell() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void testClone() {
    }
}