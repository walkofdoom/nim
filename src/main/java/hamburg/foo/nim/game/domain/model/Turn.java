package hamburg.foo.nim.game.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Turn {
    private int takeTokens;
}
