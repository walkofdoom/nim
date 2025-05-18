package hamburg.foo.nim.game.application.turn;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.Turn;
import hamburg.foo.nim.game.domain.rules.GameRules;

@Service
public class RandomTurnService implements ComputeTurnUseCase {

    @Override
    public Turn computeTurn(GameState game) {
        int tokens = RandomUtils.insecure().randomInt(GameRules.MIN_TOKENS_PER_TURN,
                Math.min(GameRules.MAX_TOKENS_PER_TURN, game.getRemainingTokens()));
        return Turn.builder().takeTokens(tokens).build();
    }

}
