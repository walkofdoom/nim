package hamburg.foo.nim.game.domain.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;
import hamburg.foo.nim.game.domain.model.Turn;

class GameRulesTest {

    @Test
    void validTurnReturnsOk() {
        GameState game = GameState.builder().remainingTokens(5).build();
        Turn turn = Turn.builder().takeTokens(2).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertTrue(result.valid());
        assertNull(result.reason());
    }

    @Test
    void turnTooLargeIsInvalid() {
        GameState game = GameState.builder().remainingTokens(5).build();
        Turn turn = Turn.builder().takeTokens(5).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertFalse(result.valid());
        assertEquals(GameRules.MSG_MAX_EXCEEDED, result.reason());
    }

    @Test
    void turnTooSmallIsInvalid() {
        GameState game = GameState.builder().remainingTokens(5).build();
        Turn turn = Turn.builder().takeTokens(0).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertFalse(result.valid());
        assertEquals(GameRules.MSG_BENEATH_MIN, result.reason());
    }

    @Test
    void turnWithTooManyTokensFails() {
        GameState game = GameState.builder().remainingTokens(2).build();
        Turn turn = Turn.builder().takeTokens(3).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertFalse(result.valid());
        assertEquals("You cannot take more tokens than available.", result.reason());
    }

    @Test
    void endedGameIsInvalid() {
        GameState game = GameState.builder().remainingTokens(0).build();
        Turn turn = Turn.builder().takeTokens(1).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertFalse(result.valid());
        assertEquals("Game has already ended.", result.reason());
    }

    @Test
    void nextTurnPlayerIsSwitched() {
        GameState game = GameState.builder().nextTurn(PlayerType.HUMAN).build();
        assertEquals(PlayerType.COMPUTER, GameRules.getNextTurnPlayer(game));
    }

    @Test
    void winnerIsPreviousPlayer() {
        GameState game = GameState.builder().nextTurn(PlayerType.HUMAN).build();
        assertEquals(PlayerType.COMPUTER, GameRules.getWinner(game));
    }
}
