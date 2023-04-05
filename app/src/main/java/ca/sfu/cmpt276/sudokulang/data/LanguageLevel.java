package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "language_level",
        indices = {
                @Index(value = "name", unique = true)
        }
)
public class LanguageLevel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final int mId;

    @NonNull
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE)
    private final String mName;

    public LanguageLevel(int id, @NonNull String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }
}