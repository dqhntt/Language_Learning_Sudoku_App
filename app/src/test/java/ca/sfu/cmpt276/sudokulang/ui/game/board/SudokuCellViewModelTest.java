package ca.sfu.cmpt276.sudokulang.ui.game.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

import ca.sfu.cmpt276.sudokulang.ui.InstantExecutorExtension;

@ExtendWith(InstantExecutorExtension.class)
class SudokuCellViewModelTest {
    private SudokuCellViewModel cell;

    @BeforeEach
    void setUp() {
        cell = new SudokuCellViewModel();
    }

    @Test
    void init() {
        for (var text : TestData.STRINGS) {
            for (var prefilled : TestData.BOOLEANS) {
                for (var error : TestData.BOOLEANS) {
                    if (error && prefilled) {
                        assertThrows(IllegalArgumentException.class, () ->
                                cell = new SudokuCellViewModel(text, prefilled, error));
                    } else {
                        cell = new SudokuCellViewModel(text, prefilled, error);
                        assertEquals(text, cell.getText().getValue());
                        assertEquals(prefilled, cell.isPrefilled().getValue());
                        assertEquals(error, cell.isErrorCell().getValue());
                        assertEquals(-1, cell.getRowIndex().getValue());
                        assertEquals(-1, cell.getColIndex().getValue());
                    }
                }
            }
        }
    }

    @Test
    void setProperties() {
        for (var text : TestData.STRINGS) {
            for (var prefilled : TestData.BOOLEANS) {
                for (var error : TestData.BOOLEANS) {
                    if (error && prefilled) {
                        assertThrows(IllegalArgumentException.class, () ->
                                cell.setProperties(text, prefilled, error));
                    } else {
                        cell.setProperties(text, prefilled, error);
                        assertEquals(text, cell.getText().getValue());
                        assertEquals(prefilled, cell.isPrefilled().getValue());
                        assertEquals(error, cell.isErrorCell().getValue());
                        assertEquals(-1, cell.getRowIndex().getValue());
                        assertEquals(-1, cell.getColIndex().getValue());
                    }
                }
            }
        }
    }

    @Test
    void setText() {
        for (var text : TestData.STRINGS) {
            cell.setText(text);
            assertEquals(text, cell.getText().getValue());
        }
    }

    @Test
    void setRowIndex() {
        for (var index : TestData.INTEGERS) {
            cell.setRowIndex(index);
            assertEquals(index, cell.getRowIndex().getValue());
        }
        for (var n : new Random().ints(100).toArray()) {
            cell.setRowIndex(n);
            assertEquals(n, cell.getRowIndex().getValue());
        }
    }

    @Test
    void setColIndex() {
        for (var index : TestData.INTEGERS) {
            cell.setColIndex(index);
            assertEquals(index, cell.getColIndex().getValue());
        }
        for (var n : new Random().ints(100).toArray()) {
            cell.setColIndex(n);
            assertEquals(n, cell.getColIndex().getValue());
        }
    }

    @Test
    void setAsErrorCell() {
        for (var prefilled : TestData.BOOLEANS) {
            cell.setProperties("", false, false);
            cell.setPrefilled(prefilled);
            for (var error : TestData.BOOLEANS) {
                if (error && prefilled) {
                    assertThrows(IllegalArgumentException.class, () ->
                            cell.setAsErrorCell(error));
                } else {
                    cell.setAsErrorCell(error);
                    assertEquals(error, cell.isErrorCell().getValue());
                }
            }
        }
    }

    @Test
    void setPrefilled() {
        for (var error : TestData.BOOLEANS) {
            cell.setProperties("", false, false);
            cell.setAsErrorCell(error);
            for (var prefilled : TestData.BOOLEANS) {
                if (prefilled && error) {
                    assertThrows(IllegalArgumentException.class, () ->
                            cell.setPrefilled(prefilled));
                } else {
                    cell.setPrefilled(prefilled);
                    assertEquals(prefilled, cell.isPrefilled().getValue());
                }
            }
        }
    }
}