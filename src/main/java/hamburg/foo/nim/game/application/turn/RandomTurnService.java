package hamburg.foo.nim.game.application.turn;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.Turn;
import hamburg.foo.nim.game.domain.rules.GameRules;

/**
 * Random computer strategy for the Nim game.
 *
 * This implementation selects a random valid move between the minimum and
 * maximum allowed tokens. It does not apply any game strategy.
 *
 * Active only if the Spring profile "random-computer" is enabled.
 */
@Service
@Profile("random-computer")
public class RandomTurnService implements ComputeTurnUseCase {

    /**
     * Computes a random move for the computer player, based on the current game
     * state.
     * The number of tokens is chosen randomly between the allowed minimum and
     * maximum, but not exceeding the remaining tokens.
     *
     * @param game Current game state
     * @return A random valid turn
     */
    @Override
    public Turn computeTurn(GameState game) {
        int tokens = RandomUtils.insecure().randomInt(GameRules.MIN_TOKENS_PER_TURN,
                Math.min(GameRules.MAX_TOKENS_PER_TURN, game.getRemainingTokens()));
        return Turn.builder().takeTokens(tokens).build();
    }

}
