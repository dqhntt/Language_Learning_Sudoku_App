package ca.sfu.cmpt276.sudokulang.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "translation",
        foreignKeys = {
                @ForeignKey(entity = Word.class,
                        parentColumns = "id",
                        childColumns = "original_word_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Word.class,
                        parentColumns = "id",
                        childColumns = "translated_word_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"original_word_id", "translated_word_id"}, unique = true)
        }
)
public class Translation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "original_word_id", index = true)
    private final int mOriginalWordId;

    @ColumnInfo(name = "translated_word_id", index = true)
    private final int mTranslatedWordId;

    @ColumnInfo(name = "is_favourite", defaultValue = "0")
    private final boolean mIsFavourite;

    public Translation(int id, int originalWordId, int translatedWordId, boolean isFavourite) {
        mId = id;
        mOriginalWordId = originalWordId;
        mTranslatedWordId = translatedWordId;
        mIsFavourite = isFavourite;
    }

    @Ignore
    public Translation(int originalWordId, int translatedWordId, boolean isFavourite) {
        mOriginalWordId = originalWordId;
        mTranslatedWordId = translatedWordId;
        mIsFavourite = isFavourite;
    }

    int getId() {
        return mId;
    }

    int getOriginalWordId() {
        return mOriginalWordId;
    }

    int getTranslatedWordId() {
        return mTranslatedWordId;
    }

    boolean isFavourite() {
        return mIsFavourite;
    }
}