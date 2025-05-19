package hamburg.foo.nim.game.application.turn;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.Turn;
import hamburg.foo.nim.game.domain.rules.GameRules;

@Service
@Profile("smart-computer")
public class SmartTurnService implements ComputeTurnUseCase {

    private static int MAGIC_STEP = GameRules.MAX_TOKENS_PER_TURN + GameRules.MIN_TOKENS_PER_TURN; // default 4

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
