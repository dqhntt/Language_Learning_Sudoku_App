package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(tableName = "language",
        indices = {
                @Index(value = {"name", "code"}, unique = true)
        }
)
public class Language {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final int mId;

    @NonNull
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE)
    private final String mName;

    @NonNull
    @ColumnInfo(name = "code", collate = ColumnInfo.NOCASE)
    private final Locale mCode; // ISO 639.

    public Language(int id, @NonNull String name, @NonNull Locale code) {
        mId = id;
        mName = name;
        mCode = code;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @NonNull
    public Locale getCode() {
        return mCode;
    }
}