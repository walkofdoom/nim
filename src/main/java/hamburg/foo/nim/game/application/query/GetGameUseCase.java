package hamburg.foo.nim.game.application.query;

import org.springframework.lang.NonNull;

import hamburg.foo.nim.game.domain.model.GameState;

public interface GetGameUseCase {

    GameState getById(@NonNull String uuid);

}
