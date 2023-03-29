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
    @ColumnInfo(name = "board_id", index = true)
    private final long mBoardId;

    @ColumnInfo(name = "native_language_id", index = true)
    private final long mNativeLanguageId;

    @ColumnInfo(name = "learning_language_id", index = true)
    private final long mLearningLanguageId;

    @ColumnInfo(name = "learning_language_level_id", index = true)
    private final long mLearningLanguageLevelId;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final long mId;

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
                boolean isCompleted, long boardId, @NonNull Cell[][] currentBoardValues,
                long nativeLanguageId, long learningLanguageId, long learningLanguageLevelId) {
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
    public Game(long id, long boardId, @NonNull Cell[][] currentBoardValues,
                long nativeLanguageId, long learningLanguageId, long learningLanguageLevelId) {
        mId = id;
        mBoardId = boardId;
        mCurrentBoardValues = currentBoardValues;
        mNativeLanguageId = nativeLanguageId;
        mLearningLanguageId = learningLanguageId;
        mLearningLanguageLevelId = learningLanguageLevelId;
    }

    public long getId() {
        return mId;
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

    public long getNativeLanguageId() {
        return mNativeLanguageId;
    }

    public long getLearningLanguageId() {
        return mLearningLanguageId;
    }

    public long getLearningLanguageLevelId() {
        return mLearningLanguageLevelId;
    }
}