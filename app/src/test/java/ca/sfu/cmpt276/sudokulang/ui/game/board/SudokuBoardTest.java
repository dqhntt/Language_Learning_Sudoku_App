package ca.sfu.cmpt276.sudokulang.ui.game.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SudokuBoardTest {
    Context appContext = ApplicationProvider.getApplicationContext();
    SudokuBoard board;

    @BeforeEach
    public void setUp() throws Exception {
        board = new SudokuBoard(appContext);
    }

    @Test
    public void setProperties() {
        board.setProperties(9, 3, 3);
        assertEquals(9, board.getBoardSize());
        assertEquals(5, board.getSubgridHeight());

    }

    @Test
    public void getNumEmptyCells() {
    }

    @Test
    public void getBoardSize() {
    }

    @Test
    public void getSubgridHeight() {
    }

    @Test
    public void getSubgridWidth() {
    }

    @Test
    public void getCells() {
    }

    @Test
    public void getValues() {
    }

    @Test
    public void setValues() {
    }

    @Test
    public void setValue() {
    }

    @Test
    public void testSetValue() {
    }

    @Test
    public void setCellProperties() {
    }

    @Test
    public void getSelectedCell() {
    }

    @Test
    public void setSelectedCell() {
    }

    @Test
    public void setNoSelectedCell() {
    }

    @Test
    public void existsCellInBoard() {
    }

    @Test
    public void existsCell() {
    }
}