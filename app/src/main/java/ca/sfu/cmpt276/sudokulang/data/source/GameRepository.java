package ca.sfu.cmpt276.sudokulang.data.source;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Game;
import ca.sfu.cmpt276.sudokulang.data.GameTranslation;
import ca.sfu.cmpt276.sudokulang.data.GameWithTranslations;

public interface GameRepository {
    void insert(Game game);

    void update(Game game);

    List<GameWithTranslations> getAllGamesWithTranslations();

    long insert(GameTranslation gameTranslation);
}