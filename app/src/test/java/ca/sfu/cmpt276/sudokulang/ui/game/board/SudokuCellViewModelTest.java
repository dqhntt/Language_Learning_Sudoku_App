package ca.sfu.cmpt276.sudokulang.ui.game.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        final var texts = new String[]{"", " ", ".", " . . ", " Cell", "llec", "CELL "};
        final var prefilledVals = new boolean[]{true, false};
        final var errorVals = new boolean[]{false, true};
        for (var text : texts) {
            for (var prefilled : prefilledVals) {
                for (var error : errorVals) {
                    if (error && prefilled) {
                        assertThrows(IllegalArgumentException.class, () ->
                                cell.setProperties(text, prefilled, error));
                    } else {
                        cell.setProperties(text, prefilled, error);
                        assertEquals(text, cell.getText().getValue());
                        assertEquals(prefilled, cell.isPrefilled().getValue());
                        assertEquals(error, cell.isErrorCell().getValue());
                    }
                }
            }
        }
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