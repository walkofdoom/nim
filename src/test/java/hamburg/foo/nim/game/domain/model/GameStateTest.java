package hamburg.foo.nim.game.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void applyTurnReducesRemainingTokens() {
        GameState game = GameState.builder().uuid("test").remainingTokens(10)
                .nextTurn(PlayerType.HUMAN).build();

        Turn turn = Turn.builder().takeTokens(3).build();
        game.applyTurn(turn);

        assertEquals(7, game.getRemainingTokens());
    }

    @Test
    void gameEndsWhenNoTokensLeft() {
        GameState game = GameState.builder().uuid("test").remainingTokens(0)
                .nextTurn(PlayerType.HUMAN).build();

        assertTrue(game.hasGameEnded());
    }

    @Test
    void gameNotEndedWhenTokensRemain() {
        GameState game = GameState.builder().uuid("test").remainingTokens(1)
                .nextTurn(PlayerType.HUMAN).build();

        assertFalse(game.hasGameEnded());
    }
}
