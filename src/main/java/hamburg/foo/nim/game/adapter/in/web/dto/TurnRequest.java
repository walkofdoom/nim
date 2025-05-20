package hamburg.foo.nim.game.adapter.in.web.dto;

import hamburg.foo.nim.game.domain.rules.GameRules;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TurnRequest(
        @Min(GameRules.MIN_TOKENS_PER_TURN) @Max(GameRules.MAX_TOKENS_PER_TURN) int takeTokens) {

}
