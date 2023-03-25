package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Arrays;

import ca.sfu.cmpt276.sudokulang.GameViewModel;

@Entity(tableName = "board",
        indices = {
                @Index(value = "prefilled_values", unique = true)
        }
)
public class BoardImpl implements Board {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final int mId;

    @NonNull
    @ColumnInfo(name = "level", collate = ColumnInfo.NOCASE)
    private final String mDifficultyLevel;

    @ColumnInfo(name = "size")
    private final int mBoardSize;

    @ColumnInfo(name = "subgrid_height")
    private final int mSubgridHeight;

    @ColumnInfo(name = "subgrid_width")
    private final int mSubgridWidth;

    @NonNull
    @ColumnInfo(name = "prefilled_values", collate = ColumnInfo.RTRIM)
    private final Cell[][] mPrefilledValues;

    @NonNull
    @ColumnInfo(name = "solved_values", collate = ColumnInfo.RTRIM)
    private final Cell[][] mSolvedValues;

    @Ignore
    @NonNull
    private final CellImpl[][] mCells;

    @Ignore
    private int mSelectedRowIndex, mSelectedColIndex;

    /**
     * @implNote Board dimension when default constructed is undefined.
     */
    @Ignore
    public BoardImpl() {
        this(0, "", 1, 1, 1,
                new CellImpl[][]{new CellImpl[]{new CellImpl()}},
                new CellImpl[][]{new CellImpl[]{new CellImpl()}});
    }

    /**
     * @apiNote Only for use with {@link GameViewModel#createEmptyBoard(int, int, int)}
     * @// TODO: Remove this since board data is coming straight from Room.
     */
    @Ignore
    public BoardImpl(int boardSize, int subgridHeight, int subgridWidth,
                     @NonNull Cell[][] currentValues) {
        this(0, "", boardSize, subgridHeight, subgridWidth,
                currentValues, currentValues);
    }

    /**
     * @apiNote DO NOT USE. For Room only.
     */
    public BoardImpl(int id, @NonNull String difficultyLevel,
                     int boardSize, int subgridHeight, int subgridWidth,
                     @NonNull Cell[][] prefilledValues, @NonNull Cell[][] solvedValues) {
        if (!isValidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
            throw new IllegalArgumentException("Invalid board dimension");
        }
        mId = id;
        mDifficultyLevel = difficultyLevel;
        mBoardSize = boardSize;
        mSubgridHeight = subgridHeight;
        mSubgridWidth = subgridWidth;
        mPrefilledValues = prefilledValues;
        mSolvedValues = solvedValues;
        mSelectedRowIndex = mSelectedColIndex = -1;
        mCells = Arrays.stream(prefilledValues)
                .map(row -> Arrays.stream(row)
                        .map(cell -> (CellImpl) cell)
                        .toArray(CellImpl[]::new))
                .toArray(CellImpl[][]::new);
    }

    private boolean isValidBoardDimension(int boardSize, int subgridHeight, int subgridWidth) {
        // Ensure sub-grids are equally divided.
        return (subgridHeight > 0 && subgridHeight <= boardSize) && (boardSize % subgridHeight == 0)
                && (subgridWidth > 0 && subgridWidth <= boardSize) && (boardSize % subgridWidth == 0);
    }

    @Override
    public int getBoardSize() {
        return mBoardSize;
    }

    @Override
    public int getSubgridHeight() {
        return mSubgridHeight;
    }

    @Override
    public int getSubgridWidth() {
        return mSubgridWidth;
    }

    @Override
    public int getSelectedRowIndex() {
        return mSelectedRowIndex;
    }

    @Override
    public int getSelectedColIndex() {
        return mSelectedColIndex;
    }

    /**
     * @param rowIndex Row index of the selected cell, -1 if none.
     * @param colIndex Column index of the selected cell, -1 if none.
     * @return {@code this}
     */
    @NonNull
    public BoardImpl setSelectedIndexes(int rowIndex, int colIndex) {
        if (rowIndex != -1 && colIndex != -1 // Selected cell is inside the board.
                && !indexesAreInsideBoard(rowIndex, colIndex)) {
            throw new IllegalArgumentException("Invalid indexes: (" + rowIndex + ", " + colIndex + ")");
        }
        mSelectedRowIndex = rowIndex;
        mSelectedColIndex = colIndex;
        return this;
    }

    @Nullable
    @Override
    public Cell getSelectedCell() {
        return indexesAreInsideBoard(mSelectedRowIndex, mSelectedColIndex)
                ? mCells[mSelectedRowIndex][mSelectedColIndex]
                : null;
    }

    private boolean indexesAreInsideBoard(int rowIndex, int colIndex) {
        return (rowIndex >= 0 && rowIndex < mCells.length)
                && (colIndex >= 0 && colIndex < mCells[0].length);
    }

    @NonNull
    @Override
    public Cell[][] getCells() {
        return mCells;
    }

    @Override
    public boolean isSolvedBoard() {
        return (getNumEmptyCells() == 0) && hasNoErrorCells();
    }

    private int getNumEmptyCells() {
        int numEmptyCells = 0;
        for (var row : mCells) {
            for (var cell : row) {
                if (cell.isEmpty()) {
                    numEmptyCells++;
                }
            }
        }
        return numEmptyCells;
    }

    private boolean hasNoErrorCells() {
        for (var row : mCells) {
            for (var cell : row) {
                if (cell.isErrorCell()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getDifficultyLevel() {
        return mDifficultyLevel;
    }

    @NonNull
    public Cell[][] getPrefilledValues() {
        return mPrefilledValues;
    }

    @NonNull
    public Cell[][] getSolvedValues() {
        return mSolvedValues;
    }
}