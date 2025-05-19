package hamburg.foo.nim.game.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hamburg.foo.nim.game.adapter.in.web.dto.CreateGameRequest;
import hamburg.foo.nim.game.adapter.in.web.dto.GameMapper;
import hamburg.foo.nim.game.adapter.in.web.dto.GameResponse;
import hamburg.foo.nim.game.adapter.in.web.dto.TurnMapper;
import hamburg.foo.nim.game.adapter.in.web.dto.TurnRequest;
import hamburg.foo.nim.game.application.create.CreateGameUseCase;
import hamburg.foo.nim.game.application.query.GetGameUseCase;
import hamburg.foo.nim.game.application.turn.ExecuteTurnCommand;
import hamburg.foo.nim.game.application.turn.ExecuteTurnUseCase;
import hamburg.foo.nim.game.domain.model.GameState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/v1/games")
public class GameController {

    private final GetGameUseCase getGame;
    private final CreateGameUseCase createGame;
    private final ExecuteTurnUseCase executeTurn;
    private final GameMapper gameMapper;
    private final TurnMapper turnMapper;

    public GameController(GetGameUseCase getGame, CreateGameUseCase createGame, ExecuteTurnUseCase executeTurn,
            GameMapper gameMapper, TurnMapper turnMapper) {
        this.getGame = getGame;
        this.createGame = createGame;
        this.executeTurn = executeTurn;
        this.gameMapper = gameMapper;
        this.turnMapper = turnMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGame(@PathVariable @NotBlank String id) {
        GameState game = getGame.getById(id);
        GameResponse gameDTO = gameMapper.mapGameToDto(game);
        return ResponseEntity.ok(gameDTO);
    }

    @PostMapping("/create")
    public GameResponse createGame(@RequestBody(required = false) CreateGameRequest request) {
        hamburg.foo.nim.game.domain.model.PlayerType startingPlayer = null;
        if (request != null) {
            startingPlayer = gameMapper.mapDtoToPlayerType(request.startingPlayer());
        }
        GameState game = createGame.createNewGame(startingPlayer);
        return gameMapper.mapGameToDto(game);
    }

    @PostMapping("/{id}/turn")
    public ResponseEntity<GameResponse> makeTurn(@PathVariable @NotBlank String id,
            @RequestBody @Valid TurnRequest turnRequest) {
        ExecuteTurnCommand command = new ExecuteTurnCommand(id, turnMapper.mapDTOtoTurn(turnRequest));
        executeTurn.executeTurn(command);
        return getGame(id);
    }

}
