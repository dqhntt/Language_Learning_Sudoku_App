package ca.sfu.cmpt276.sudokulang.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "game_translation",
        primaryKeys = {"game_id", "translation_id"},
        foreignKeys = {
                @ForeignKey(entity = Game.class,
                        parentColumns = "id",
                        childColumns = "game_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Translation.class,
                        parentColumns = "id",
                        childColumns = "translation_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE)
        }
)
public class GameTranslation {
    @ColumnInfo(name = "game_id", index = true)
    private final long mGameId;

    @ColumnInfo(name = "translation_id", index = true)
    private final long mTranslationId;

    public GameTranslation(long gameId, long translationId) {
        mGameId = gameId;
        mTranslationId = translationId;
    }

    public long getGameId() {
        return mGameId;
    }

    public long getTranslationId() {
        return mTranslationId;
    }
}