package hamburg.foo.nim.game.application.turn;

import hamburg.foo.nim.game.domain.model.Turn;

public record ExecuteTurnCommand(String gameId, Turn turn) {

}
