package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "word",
        foreignKeys = {
                @ForeignKey(entity = Language.class,
                        parentColumns = "id",
                        childColumns = "language_id",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = LanguageLevel.class,
                        parentColumns = "id",
                        childColumns = "language_level_id",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"text", "language_id", "language_level_id"}, unique = true)
        }
)
public class Word {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "text", collate = ColumnInfo.RTRIM)
    private final String mText;

    @ColumnInfo(name = "language_id", index = true)
    private final int mLanguageId;

    @ColumnInfo(name = "language_level_id", index = true)
    private final int mLanguageLevelId;

    public Word(int id, @NonNull String text, int languageId, int languageLevelId) {
        mId = id;
        mText = text;
        mLanguageId = languageId;
        mLanguageLevelId = languageLevelId;
    }

    @Ignore
    public Word(@NonNull String text, int languageId, int languageLevelId) {
        mText = text;
        mLanguageId = languageId;
        mLanguageLevelId = languageLevelId;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getText() {
        return mText;
    }

    public int getLanguageId() {
        return mLanguageId;
    }

    public int getLanguageLevelId() {
        return mLanguageLevelId;
    }
}