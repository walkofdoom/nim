package hamburg.foo.nim.game.application.turn;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;
import hamburg.foo.nim.game.domain.model.Turn;
import hamburg.foo.nim.game.domain.rules.GameRules;
import hamburg.foo.nim.game.domain.rules.TurnValidationResult;

@ExtendWith(MockitoExtension.class)
public class ComputeTurnUseCaseTest {

    @InjectMocks
    RandomTurnService service;

    @Test
    void computeValidTurn() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(5).nextTurn(PlayerType.COMPUTER).build();

        Turn turn = service.computeTurn(game);

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertTrue(result.valid());
    }
}
