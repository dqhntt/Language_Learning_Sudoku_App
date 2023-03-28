package ca.sfu.cmpt276.sudokulang.data.source;

import androidx.annotation.NonNull;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Game;
import ca.sfu.cmpt276.sudokulang.data.GameTranslation;
import ca.sfu.cmpt276.sudokulang.data.GameWithTranslations;

public interface GameRepository {
    long generateId();

    void insert(@NonNull Game game);

    void update(@NonNull Game game);

    @NonNull
    List<GameWithTranslations> getAllGamesWithTranslations();

    void insert(@NonNull GameTranslation gameTranslation);

    void insert(@NonNull List<GameTranslation> gameTranslations);
}