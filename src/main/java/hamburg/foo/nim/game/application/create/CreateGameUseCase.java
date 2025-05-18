package hamburg.foo.nim.game.application.create;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;
import jakarta.annotation.Nullable;

public interface CreateGameUseCase {

    GameState createNewGame(@Nullable PlayerType startingPlayer);

}