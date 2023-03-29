package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "game",
        foreignKeys = {
                @ForeignKey(entity = BoardImpl.class,
                        parentColumns = "id",
                        childColumns = "board_id",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "start_time", unique = true)
        }
)
public class Game {
    @ColumnInfo(name = "board_id", index = true)
    private final long mBoardId;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @NonNull
    @ColumnInfo(name = "start_time", defaultValue = "CURRENT_TIMESTAMP")
    private Date mStartTime = new Date();

    @ColumnInfo(name = "time_duration", defaultValue = "0")
    private long mTimeDuration = 0;

    @ColumnInfo(name = "is_completed", defaultValue = "0")
    private boolean mIsCompleted = false;

    @NonNull
    @ColumnInfo(name = "current_board_values", collate = ColumnInfo.RTRIM)
    private Cell[][] mCurrentBoardValues;

    public Game(long id, @NonNull Date startTime, long timeDuration,
                boolean isCompleted, long boardId, @NonNull Cell[][] currentBoardValues) {
        mId = id;
        mStartTime = startTime;
        mTimeDuration = timeDuration;
        mIsCompleted = isCompleted;
        mBoardId = boardId;
        mCurrentBoardValues = currentBoardValues;
    }

    @Ignore
    public Game(long boardId, @NonNull Cell[][] currentBoardValues) {
        mId = 0;
        mBoardId = boardId;
        mCurrentBoardValues = currentBoardValues;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        assert id >= 0;
        mId = id;
    }

    @NonNull
    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(@NonNull Date startTime) {
        mStartTime = startTime;
    }

    public long getTimeDuration() {
        return mTimeDuration;
    }

    @NonNull
    public Game setTimeDuration(long timeDuration) {
        mTimeDuration = timeDuration;
        return this;
    }

    public boolean isCompleted() {
        return mIsCompleted;
    }

    @NonNull
    public Game setCompleted(boolean completed) {
        mIsCompleted = completed;
        return this;
    }

    public long getBoardId() {
        return mBoardId;
    }

    @NonNull
    public Cell[][] getCurrentBoardValues() {
        return mCurrentBoardValues;
    }

    @NonNull
    public Game setCurrentBoardValues(@NonNull Cell[][] currentBoardValues) {
        mCurrentBoardValues = currentBoardValues;
        return this;
    }
}