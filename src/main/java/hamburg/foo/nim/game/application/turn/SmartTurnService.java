package hamburg.foo.nim.game.application.turn;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.Turn;
import hamburg.foo.nim.game.domain.rules.GameRules;

/**
 * Smart computer strategy for the Misère Nim game.
 * 
 * This implementation attempts to force the opponent into losing positions, based on the
 * mathematical structure of Misère Nim with fixed token steps.
 * 
 * Active only if the Spring profile "smart-computer" is enabled.
 */
@Service
@Profile("smart-computer")
public class SmartTurnService implements ComputeTurnUseCase {

    /**
     * The target step size to reach "losing positions" (1, 5, 9, ...). For a 1–3 token game, this
     * is 4.
     */
    private static int MAGIC_STEP = GameRules.MAX_TOKENS_PER_TURN + GameRules.MIN_TOKENS_PER_TURN;

    /**
     * Computes the optimal turn based on the current number of remaining tokens. The goal is to
     * leave the opponent with a number of tokens equal to a "losing state".
     *
     * @param game Current game state
     * @return A turn (1–3 tokens) based on calculated strategy or random fallback
     */
    @Override
    public Turn computeTurn(GameState game) {
        int tokens;

        if (game.getRemainingTokens() <= GameRules.MIN_TOKENS_PER_TURN) {
            // we lost :(
            tokens = GameRules.MIN_TOKENS_PER_TURN;

        } else if (game.getRemainingTokens() <= MAGIC_STEP) {
            // we can end the game, take all tokens except minimum
            tokens = game.getRemainingTokens() - GameRules.MIN_TOKENS_PER_TURN;

        } else if (game.getRemainingTokens() % MAGIC_STEP != GameRules.MIN_TOKENS_PER_TURN) {
            // we are in control, keep returning loosing remaining tokens: 5 and 9
            tokens = (game.getRemainingTokens() - GameRules.MIN_TOKENS_PER_TURN) % MAGIC_STEP;

        } else {
            // we basically lost, but can still use random and hope for opponent mistakes
            tokens = RandomUtils.insecure().randomInt(GameRules.MIN_TOKENS_PER_TURN,
                    Math.min(GameRules.MAX_TOKENS_PER_TURN, game.getRemainingTokens()));
        }

        return Turn.builder().takeTokens(tokens).build();
    }

}
