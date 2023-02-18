package ca.sfu.cmpt276.sudokulang.ui.game.board;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(InstantExecutorExtension.class)
class SudokuCellViewModelTest {
    private SudokuCellViewModel cell;

    @BeforeEach
    void setUp() {
        cell = new SudokuCellViewModel();
    }

    @Test
    void setProperties() {
        cell.setProperties("", false, false);
        assertEquals("", cell.getText().getValue());
        assertEquals(false, cell.isPrefilled().getValue());
        assertEquals(false, cell.isErrorCell().getValue());
        cell.setProperties("Not_Empty", true, false);
        assertEquals("Not_Empty", cell.getText().getValue());
        assertEquals(true, cell.isPrefilled().getValue());
        assertEquals(false, cell.isErrorCell().getValue());
    }

    @Test
    void getText() {
        cell.setText("");
        assertEquals("", cell.getText().getValue());
        cell.setText("Not_Empty");
        assertEquals("Not_Empty", cell.getText().getValue());
    }

    @Test
    void setText() {
        cell.setText("");
        assertEquals("", cell.getText().getValue());
        cell.setText("Null");
        assertEquals("Null", cell.getText().getValue());
    }

    @Test
    void getRowIndex() {
        cell.setRowIndex(0);
        assertEquals(0, cell.getRowIndex().getValue());
        cell.setRowIndex(3);
        assertEquals(3, cell.getRowIndex().getValue());
        cell.setRowIndex(-1);
        assertEquals(-1, cell.getRowIndex().getValue());
    }

    @Test
    void setRowIndex() {
        cell.setRowIndex(0);
        assertEquals(0, cell.getRowIndex().getValue());
        cell.setRowIndex(5);
        assertEquals(5, cell.getRowIndex().getValue());
        cell.setRowIndex(-1);
        assertEquals(-1, cell.getRowIndex().getValue());
    }

    @Test
    void getColIndex() {
        cell.setColIndex(0);
        assertEquals(0, cell.getColIndex().getValue());
        cell.setColIndex(5);
        assertEquals(5, cell.getColIndex().getValue());
        cell.setColIndex(-1);
        assertEquals(-1, cell.getColIndex().getValue());
    }

    @Test
    void setColIndex() {
        cell.setColIndex(0);
        assertEquals(0, cell.getColIndex().getValue());
        cell.setColIndex(3);
        assertEquals(3, cell.getColIndex().getValue());
        cell.setColIndex(-1);
        assertEquals(-1, cell.getColIndex().getValue());
    }

    @Test
    void isErrorCell() {
        cell.setAsErrorCell(false);
        assertEquals(false, cell.isErrorCell().getValue());
        cell.setAsErrorCell(true);
        assertEquals(true, cell.isErrorCell().getValue());
    }

    @Test
    void setAsErrorCell() {
        cell.setAsErrorCell(true);
        assertEquals(true, cell.isErrorCell().getValue());
        cell.setAsErrorCell(false);
        assertEquals(false, cell.isErrorCell().getValue());
    }

    @Test
    void isPrefilled() {
        cell.setPrefilled(false);
        assertEquals(false, cell.isPrefilled().getValue());
        cell.setPrefilled(true);
        assertEquals(true, cell.isPrefilled().getValue());
    }

    @Test
    void setPrefilled() {
        cell.setPrefilled(true);
        assertEquals(true, cell.isPrefilled().getValue());
        cell.setPrefilled(false);
        assertEquals(false, cell.isPrefilled().getValue());
    }
}