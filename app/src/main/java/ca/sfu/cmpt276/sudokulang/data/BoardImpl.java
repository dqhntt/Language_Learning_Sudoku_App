package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "board",
        indices = {
                @Index(value = "prefilled_values", unique = true)
        }
)
public class BoardImpl implements Board {
    @ColumnInfo(name = "size")
    private final int mBoardSize;

    @ColumnInfo(name = "subgrid_height")
    private final int mSubgridHeight;

    @ColumnInfo(name = "subgrid_width")
    private final int mSubgridWidth;

    @Ignore
    @NonNull
    private final CellImpl[][] mCells;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "level", collate = ColumnInfo.NOCASE)
    private String mDifficultyLevel;

    @NonNull
    @ColumnInfo(name = "prefilled_values", collate = ColumnInfo.RTRIM)
    private Cell[][] mPrefilledValues;

    @NonNull
    @ColumnInfo(name = "solved_values", collate = ColumnInfo.RTRIM)
    private Cell[][] mSolvedValues;

    @Ignore
    private int mSelectedRowIndex, mSelectedColIndex;

    /**
     * @implNote Board dimension when default constructed is undefined.
     */
    @Ignore
    public BoardImpl() {
        this(1, 1, 1,
                -1, -1,
                new CellImpl[][]{new CellImpl[]{new CellImpl()}});
    }

    @Ignore
    public BoardImpl(int boardSize, int subgridHeight, int subgridWidth,
                     int selectedRowIndex, int selectedColIndex,
                     @NonNull CellImpl[][] cells) {
        if (!isValidBoardDimension(boardSize, subgridHeight, subgridWidth)) {
            throw new IllegalArgumentException("Invalid board dimension");
        }
        mBoardSize = boardSize;
        mSubgridHeight = subgridHeight;
        mSubgridWidth = subgridWidth;
        mSelectedRowIndex = selectedRowIndex;
        mSelectedColIndex = selectedColIndex;
        mCells = cells;
    }

    /**
     * @apiNote For Room only.
     */
    public BoardImpl(int id, @NonNull String difficultyLevel,
                     int boardSize, int subgridHeight, int subgridWidth,
                     @NonNull Cell[][] prefilledValues, @NonNull Cell[][] solvedValues) {
        mId = id;
        mDifficultyLevel = difficultyLevel;
        mBoardSize = boardSize;
        mSubgridHeight = subgridHeight;
        mSubgridWidth = subgridWidth;
        mPrefilledValues = prefilledValues;
        mSolvedValues = solvedValues;
        mSelectedRowIndex = mSelectedColIndex = -1;
        mCells = (CellImpl[][]) prefilledValues;
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

    public BoardImpl setSelectedRowIndex(int rowIndex) {
        if (!areValidIndexes(rowIndex, mSelectedColIndex)) {
            throw new IllegalArgumentException("Invalid row index");
        }
        mSelectedRowIndex = rowIndex;
        return this;
    }

    @Override
    public int getSelectedColIndex() {
        return mSelectedColIndex;
    }

    public BoardImpl setSelectedColIndex(int colIndex) {
        if (!areValidIndexes(mSelectedRowIndex, colIndex)) {
            throw new IllegalArgumentException("Invalid column index");
        }
        mSelectedColIndex = colIndex;
        return this;
    }

    @Nullable
    @Override
    public Cell getSelectedCell() {
        return areValidIndexes(mSelectedRowIndex, mSelectedColIndex)
                ? mCells[mSelectedRowIndex][mSelectedColIndex]
                : null;
    }

    private boolean areValidIndexes(int rowIndex, int colIndex) {
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