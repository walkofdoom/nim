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
    }

    @Test
    void tooManyTokensIsInvalid() {
        GameState game = GameState.builder().remainingTokens(5).build();
        Turn turn = Turn.builder().takeTokens(5).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertFalse(result.valid());
        assertNotNull(result.reason());
    }

    @Test
    void notEnoughTokensIsInvalid() {
        GameState game = GameState.builder().remainingTokens(5).build();
        Turn turn = Turn.builder().takeTokens(0).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertFalse(result.valid());
        assertNotNull(result.reason());
    }

    @Test
    void moreThanAvailableTokensFails() {
        GameState game = GameState.builder().remainingTokens(2).build();
        Turn turn = Turn.builder().takeTokens(3).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertFalse(result.valid());
        assertNotNull(result.reason());
    }

    @Test
    void endedGameIsInvalid() {
        GameState game = GameState.builder().remainingTokens(0).build();
        Turn turn = Turn.builder().takeTokens(1).build();

        TurnValidationResult result = GameRules.validateTurn(game, turn);
        assertFalse(result.valid());
        assertNotNull(result.reason());
    }

    @Test
    void nextTurnPlayerIsComputer() {
        GameState game = GameState.builder().nextTurn(PlayerType.HUMAN).build();
        assertEquals(PlayerType.COMPUTER, GameRules.getNextTurnPlayer(game));
    }

    @Test
    void nextTurnPlayerIsHuman() {
        GameState game = GameState.builder().nextTurn(PlayerType.COMPUTER).build();
        assertEquals(PlayerType.HUMAN, GameRules.getNextTurnPlayer(game));
    }

    @Test
    void winnerIsPreviousPlayer() {
        GameState game = GameState.builder().remainingTokens(0)
                .nextTurn(PlayerType.HUMAN).build();
        assertEquals(PlayerType.COMPUTER, GameRules.getWinner(game));
    }
}
