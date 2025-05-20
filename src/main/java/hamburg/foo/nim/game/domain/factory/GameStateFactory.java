package hamburg.foo.nim.game.domain.factory;

import java.util.UUID;

import org.springframework.stereotype.Component;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;

@Component
public class GameStateFactory {

    public GameState createNewGame(int initialTokens, PlayerType startingPlayer) {
        return GameState.builder().uuid(UUID.randomUUID().toString()).remainingTokens(initialTokens)
                .nextTurn(startingPlayer).build();
    }
}
