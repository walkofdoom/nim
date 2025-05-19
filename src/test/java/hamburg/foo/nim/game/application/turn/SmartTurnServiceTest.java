package hamburg.foo.nim.game.application.turn;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class SmartTurnServiceTest {

    @InjectMocks
    SmartTurnService service;

    @Test
    void reduceToMagicNumberFrom8() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(8).nextTurn(PlayerType.COMPUTER).build();

        Turn turn = service.computeTurn(game);

        assertEquals(3, turn.getTakeTokens());
    }

    @Test
    void reduceToMagicNumberFrom7() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(7).nextTurn(PlayerType.COMPUTER).build();

        Turn turn = service.computeTurn(game);

        assertEquals(2, turn.getTakeTokens());
    }

    @Test
    void reduceToMagicNumberFrom10() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(10).nextTurn(PlayerType.COMPUTER).build();

        Turn turn = service.computeTurn(game);

        assertEquals(1, turn.getTakeTokens());
    }

    @Test
    void endGameFrom3() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(3).nextTurn(PlayerType.COMPUTER).build();

        Turn turn = service.computeTurn(game);

        assertEquals(2, turn.getTakeTokens());
    }

    @Test
    void validTurnFrom5() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(5).nextTurn(PlayerType.COMPUTER).build();

        Turn turn = service.computeTurn(game);

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertTrue(result.valid());
    }

    @Test
    void validTurnFrom1() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(1).nextTurn(PlayerType.COMPUTER).build();

        Turn turn = service.computeTurn(game);

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertTrue(result.valid());
    }
}
