package hamburg.foo.nim.game.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameState {
    private String uuid;
    private PlayerType nextTurn;
    private PlayerType winner;
    private int remainingTokens;

    public boolean hasGameEnded() {
        return getRemainingTokens() <= 0;
    }

    public void applyTurn(Turn turn) {
        this.remainingTokens -= turn.getTakeTokens();
    }
}
