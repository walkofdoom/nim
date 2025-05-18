package hamburg.foo.nim.game.domain.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;

class GameStateFactoryTest {

    private final GameStateFactory factory = new GameStateFactory();

    @Test
    void createsGameWithGivenTokensAndPlayer() {
        GameState game = factory.createNewGame(10, PlayerType.HUMAN);

        assertNotNull(game.getUuid());
        assertEquals(10, game.getRemainingTokens());
        assertEquals(PlayerType.HUMAN, game.getNextTurn());
        assertNull(game.getWinner());
    }
}
