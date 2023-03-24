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
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Language.class,
                        parentColumns = "id",
                        childColumns = "native_language_id",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Language.class,
                        parentColumns = "id",
                        childColumns = "learning_language_id",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = LanguageLevel.class,
                        parentColumns = "id",
                        childColumns = "learning_language_level_id",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "start_time", unique = true)
        }
)
public class Game {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "start_time", defaultValue = "CURRENT_TIMESTAMP")
    private Date mStartTime;

    @ColumnInfo(name = "time_duration", defaultValue = "0")
    private long mTimeDuration;

    @ColumnInfo(name = "is_completed", defaultValue = "0")
    private boolean mIsCompleted;

    @ColumnInfo(name = "board_id", index = true)
    private final int mBoardId;

    @NonNull
    @ColumnInfo(name = "current_board_values", collate = ColumnInfo.RTRIM)
    private CellImpl[][] mCurrentBoardValues;

    @ColumnInfo(name = "native_language_id", index = true)
    private final int mNativeLanguageId;

    @ColumnInfo(name = "learning_language_id", index = true)
    private final int mLearningLanguageId;

    @ColumnInfo(name = "learning_language_level_id", index = true)
    private final int mLearningLanguageLevelId;

    public Game(int id, @NonNull Date startTime, long timeDuration,
                boolean isCompleted, int boardId, @NonNull CellImpl[][] currentBoardValues,
                int nativeLanguageId, int learningLanguageId, int learningLanguageLevelId) {
        mId = id;
        mStartTime = startTime;
        mTimeDuration = timeDuration;
        mIsCompleted = isCompleted;
        mBoardId = boardId;
        mCurrentBoardValues = currentBoardValues;
        mNativeLanguageId = nativeLanguageId;
        mLearningLanguageId = learningLanguageId;
        mLearningLanguageLevelId = learningLanguageLevelId;
    }

    @Ignore
    public Game(int boardId, @NonNull CellImpl[][] currentBoardValues,
                int nativeLanguageId, int learningLanguageId, int learningLanguageLevelId) {
        mBoardId = boardId;
        mCurrentBoardValues = currentBoardValues;
        mNativeLanguageId = nativeLanguageId;
        mLearningLanguageId = learningLanguageId;
        mLearningLanguageLevelId = learningLanguageLevelId;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public Date getStartTime() {
        return mStartTime;
    }

    public long getTimeDuration() {
        return mTimeDuration;
    }

    public void setTimeDuration(long timeDuration) {
        mTimeDuration = timeDuration;
    }

    public boolean isCompleted() {
        return mIsCompleted;
    }

    public void setCompleted(boolean completed) {
        mIsCompleted = completed;
    }

    public int getBoardId() {
        return mBoardId;
    }

    public CellImpl[][] getCurrentBoardValues() {
        return mCurrentBoardValues;
    }

    public void setCurrentBoardValues(@NonNull CellImpl[][] currentBoardValues) {
        mCurrentBoardValues = currentBoardValues;
    }

    public int getNativeLanguageId() {
        return mNativeLanguageId;
    }

    public int getLearningLanguageId() {
        return mLearningLanguageId;
    }

    public int getLearningLanguageLevelId() {
        return mLearningLanguageLevelId;
    }
}