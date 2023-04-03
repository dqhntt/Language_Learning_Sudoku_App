//package ca.sfu.cmpt276.sudokulang.ui.game.board;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Random;
//
//class BoardUiStateTest {
//    private BoardUiState board;
//
//    @BeforeEach
//    void setup() {
//        board = new BoardUiState();
//    }
//
//    @Test
//    void constructorAutoTest() {
//        for (var boardSize : TestData.INTEGERS) {
//            for (var subgridHeight : TestData.INTEGERS) {
//                for (var subgridWidth : TestData.INTEGERS) {
//                    for (var rowIndex : TestData.INTEGERS) {
//                        for (var colIndex : TestData.INTEGERS) {
//                            if (isInvalidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
//                                assertThrows(IllegalArgumentException.class, () ->
//                                        board = new BoardUiState(
//                                                boardSize, subgridHeight, subgridWidth,
//                                                rowIndex, colIndex, null
//                                        )
//                                );
//                            } else {
//                                board = new BoardUiState(
//                                        boardSize, subgridHeight, subgridWidth,
//                                        rowIndex, colIndex, null
//                                );
//                                assertEquals(boardSize, board.getBoardSize());
//                                assertEquals(subgridHeight, board.getSubgridHeight());
//                                assertEquals(subgridWidth, board.getSubgridWidth());
//                                assertEquals(rowIndex, board.getSelectedRowIndex());
//                                assertEquals(colIndex, board.getSelectedColIndex());
//                                assertNull(board.getCells());
//                                assertThrows(NullPointerException.class, () -> board.isSolvedBoard());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Test
//    void constructorRandTest() {
//        for (var boardSize : new Random().ints(10, -5, 35).toArray()) {
//            for (var subgridHeight : new Random().ints(10, -5, 35).toArray()) {
//                for (var subgridWidth : new Random().ints(10, -5, 35).toArray()) {
//                    for (var rowIndex : new Random().ints(10, -5, 35).toArray()) {
//                        for (var colIndex : new Random().ints(10, -5, 35).toArray()) {
//                            if (isInvalidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
//                                assertThrows(IllegalArgumentException.class, () ->
//                                        board = new BoardUiState(
//                                                boardSize, subgridHeight, subgridWidth,
//                                                rowIndex, colIndex, null
//                                        )
//                                );
//                            } else {
//                                {
//                                    final var cells = getFilledCellsMatrix(boardSize, boardSize);
//                                    board = new BoardUiState(
//                                            boardSize, subgridHeight, subgridWidth,
//                                            rowIndex, colIndex, cells
//                                    );
//                                    assertEquals(boardSize, board.getBoardSize());
//                                    assertEquals(subgridHeight, board.getSubgridHeight());
//                                    assertEquals(subgridWidth, board.getSubgridWidth());
//                                    assertEquals(rowIndex, board.getSelectedRowIndex());
//                                    assertEquals(colIndex, board.getSelectedColIndex());
//                                    assertEquals(cells, board.getCells());
//                                    if (areValidIndexes(rowIndex, colIndex, boardSize)) {
//                                        assertNotNull(board.getSelectedCell());
//                                    } else {
//                                        assertNull(board.getSelectedCell());
//                                    }
//                                    assertTrue(board.isSolvedBoard());
//                                }
//                                {
//                                    final var cells = getFilledCellsWithErrorsMatrix(boardSize, boardSize);
//                                    board = new BoardUiState(
//                                            boardSize, subgridHeight, subgridWidth,
//                                            rowIndex, colIndex, cells
//                                    );
//                                    assertEquals(cells, board.getCells());
//                                    assertFalse(board.isSolvedBoard());
//                                }
//                                {
//                                    final var cells = getUnfilledCellsMatrix(boardSize, boardSize);
//                                    board = new BoardUiState(
//                                            boardSize, subgridHeight, subgridWidth,
//                                            rowIndex, colIndex, cells
//                                    );
//                                    assertEquals(cells, board.getCells());
//                                    assertFalse(board.isSolvedBoard());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private boolean isInvalidBoardDimension(int boardSize, int subgridHeight, int subgridWidth) {
//        return (subgridHeight <= 0 || subgridHeight > boardSize) || (boardSize % subgridHeight != 0)
//                || (subgridWidth <= 0 || subgridWidth > boardSize) || (boardSize % subgridWidth != 0);
//    }
//
//    private boolean areValidIndexes(int rowIndex, int colIndex, int boardSize) {
//        return (rowIndex >= 0 && rowIndex < boardSize)
//                && (colIndex >= 0 && colIndex < boardSize);
//    }
//
//    private CellUiState[][] getFilledCellsMatrix(int height, int width) {
//        var cells = new CellUiState[height][width];
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                cells[i][j] = new CellUiState("filled", new Random().nextBoolean(), false);
//            }
//        }
//        return cells;
//    }
//
//    private CellUiState[][] getUnfilledCellsMatrix(int height, int width) {
//        var cells = new CellUiState[height][width];
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                final boolean prefilled = new Random().nextBoolean();
//                final boolean error = !prefilled && new Random().nextBoolean();
//                cells[i][j] = new CellUiState("", prefilled, error);
//            }
//        }
//        return cells;
//    }
//
//    private CellUiState[][] getFilledCellsWithErrorsMatrix(int height, int width) {
//        var cells = new CellUiState[height][width];
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                final boolean error = new Random().nextBoolean();
//                final boolean prefilled = !error && new Random().nextBoolean();
//                cells[i][j] = new CellUiState("text", prefilled, error);
//            }
//        }
//        return cells;
//    }
//}