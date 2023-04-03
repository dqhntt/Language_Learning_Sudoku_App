//package ca.sfu.cmpt276.sudokulang.ui.game.board;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//class CellUiStateTest {
//    private CellUiState cell;
//
//    @BeforeEach
//    void setUp() {
//        cell = new CellUiState();
//    }
//
//    @Test
//    void defaultConstructor() {
//        cell = new CellUiState();
//        assertEquals("", cell.getText());
//        assertFalse(cell.isErrorCell());
//        assertFalse(cell.isPrefilled());
//    }
//
//    @Test
//    void constructor() {
//        for (var text : TestData.STRINGS) {
//            for (var prefilled : TestData.BOOLEANS) {
//                for (var error : TestData.BOOLEANS) {
//                    if (error && prefilled) {
//                        assertThrows(IllegalArgumentException.class, () ->
//                                cell = new CellUiState(text, prefilled, error));
//                    } else {
//                        cell = new CellUiState(text, prefilled, error);
//                        assertEquals(text, cell.getText());
//                        assertEquals(prefilled, cell.isPrefilled());
//                        assertEquals(error, cell.isErrorCell());
//                    }
//                }
//            }
//        }
//    }
//}