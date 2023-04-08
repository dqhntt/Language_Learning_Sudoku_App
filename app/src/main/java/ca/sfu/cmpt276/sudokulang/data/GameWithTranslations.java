package ca.sfu.cmpt276.sudokulang.data;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

// See: https://stackoverflow.com/questions/58290052/room-junction-pojo
public class GameWithTranslations {
    @Embedded
    public Game mGame;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = GameTranslation.class,
                    parentColumn = "game_id",
                    entityColumn = "translation_id"
            )
    )
    public List<Translation> mTranslations;
}