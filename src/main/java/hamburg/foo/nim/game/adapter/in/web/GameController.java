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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Tag(name = "Games", description = "Endpoints for playing and managing a Nim game")
@RestController
@RequestMapping("/v1/games")
public class GameController {

    private static final String EXAMPLE_RESPONSE_OK = """
            {
              "id": "61c3b103-49e0-4d06-8d41-ad78bfdc3521",
              "nextTurn": "HUMAN",
              "winner": null,
              "remainingTokens": 12
            }
            """;

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

    @Operation(summary = "Get current game state", description = "Returns the current state of a Nim game by its unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game found and returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponse.class),
                            examples = @ExampleObject(value = EXAMPLE_RESPONSE_OK))),
            @ApiResponse(responseCode = "404", description = "Game ID not found",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGame(
            @Parameter(description = "Game ID", required = true) @PathVariable @NotBlank String id) {
        GameState game = getGame.getById(id);
        GameResponse gameDTO = gameMapper.mapGameToDto(game);
        return ResponseEntity.ok(gameDTO);
    }

    @Operation(summary = "Create new game", description = "Creates a new Nim game. Optionally specify which player should start the game.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New game created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponse.class),
                            examples = @ExampleObject(value = EXAMPLE_RESPONSE_OK)))
    })
    @PostMapping("/create")
    public GameResponse createGame(
            @Parameter(description = "Optional starting player (COMPUTER or HUMAN)") @RequestBody(required = false) CreateGameRequest request) {
        hamburg.foo.nim.game.domain.model.PlayerType startingPlayer = null;
        if (request != null) {
            startingPlayer = gameMapper.mapDtoToPlayerType(request.startingPlayer());
        }
        GameState game = createGame.createNewGame(startingPlayer);
        return gameMapper.mapGameToDto(game);
    }

    @Operation(summary = "Make a move in the game", description = "Submits a turn for the current game and returns the updated game state.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Turn applied and updated game returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponse.class),
                            examples = @ExampleObject(value = EXAMPLE_RESPONSE_OK))),
            @ApiResponse(responseCode = "400", description = "Invalid turn submitted",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Game ID not found",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("/{id}/turn")
    public ResponseEntity<GameResponse> makeTurn(
            @Parameter(description = "Game ID", required = true) @PathVariable @NotBlank String id,
            @Parameter(description = "Player's move (1 to 3 tokens)") @RequestBody @Valid TurnRequest turnRequest) {
        ExecuteTurnCommand command = new ExecuteTurnCommand(id, turnMapper.mapDTOtoTurn(turnRequest));
        executeTurn.executeTurn(command);
        return getGame(id);
    }

}
