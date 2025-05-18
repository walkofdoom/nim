package hamburg.foo.nim.game.application.turn;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.Turn;

public interface ComputeTurnUseCase {
    public Turn computeTurn(GameState game);
}
