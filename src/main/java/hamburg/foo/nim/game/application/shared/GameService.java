package hamburg.foo.nim.game.application.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import hamburg.foo.nim.game.adapter.out.persistence.GameSessionRepository;
import hamburg.foo.nim.game.application.create.CreateGameUseCase;
import hamburg.foo.nim.game.application.query.GetGameUseCase;
import hamburg.foo.nim.game.application.turn.ComputeTurnUseCase;
import hamburg.foo.nim.game.application.turn.ExecuteTurnCommand;
import hamburg.foo.nim.game.application.turn.ExecuteTurnUseCase;
import hamburg.foo.nim.game.domain.exception.GameNotFoundException;
import hamburg.foo.nim.game.domain.exception.InvalidTurnException;
import hamburg.foo.nim.game.domain.factory.GameStateFactory;
import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;
import hamburg.foo.nim.game.domain.model.Turn;
import hamburg.foo.nim.game.domain.rules.GameRules;
import hamburg.foo.nim.game.domain.rules.TurnValidationResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class responsible for managing the game logic and flow.
 * 
 * <p>
 * Implements use cases for creating a new game, executing a turn, and retrieving the game state.
 * </p>
 * 
 * <p>
 * This service manages game state persistence via {@link GameSessionRepository}, creates new game
 * instances through {@link GameStateFactory}, and delegates computer turn calculations to a
 * {@link ComputeTurnUseCase} implementation.
 * </p>
 * 
 * <p>
 * Main responsibilities:
 * </p>
 * <ul>
 * <li>Initialize a new game with configurable starting tokens and starting player</li>
 * <li>Validate, apply, and update the game state for player turns</li>
 * <li>Automatically execute the computer's turn when it is the computer's move</li>
 * <li>Detect game end and determine the winner</li>
 * </ul>
 * 
 * <p>
 * Configurable properties:
 * </p>
 * <ul>
 * <li>{@code startingTokens} – the number of tokens each game starts with</li>
 * <li>{@code defaultStartingPlayer} – the default player who starts the game</li>
 * </ul>
 */
@Slf4j
@Service
public class GameService implements CreateGameUseCase, ExecuteTurnUseCase, GetGameUseCase {

    private final GameStateFactory gameStateFactory;

    private final ComputeTurnUseCase computeTurn;

    private final GameSessionRepository sessionRepository;

    @Value("${game.starting-tokens}")
    private int startingTokens;

    @Value("${game.default-starting-player}")
    private PlayerType defaultStartingPlayer;

    public GameService(GameStateFactory gameStateFactory, ComputeTurnUseCase computeTurn,
            GameSessionRepository sessionRepository) {
        this.gameStateFactory = gameStateFactory;
        this.computeTurn = computeTurn;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public GameState getById(@NonNull String uuid) {
        return sessionRepository.find(uuid).orElseThrow(() -> new GameNotFoundException(uuid));
    }

    @Override
    public GameState createNewGame(PlayerType startingPlayer) {
        if (startingPlayer == null) {
            startingPlayer = this.defaultStartingPlayer;
        }
        GameState game = gameStateFactory.createNewGame(startingTokens, startingPlayer);
        sessionRepository.save(game);
        log.info("New game created: {}", game.getUuid());
        applyNextComputerTurn(game);
        return game;
    }

    /**
     * Executes a player turn by validating it and updating the game state. If the game continues
     * and it's the computer's turn, the computer move is applied automatically.
     *
     * @param command The player's move, including the game ID and number of tokens taken.
     * @throws InvalidTurnException if the submitted turn is not valid (e.g. too many tokens)
     */
    @Override
    public void executeTurn(ExecuteTurnCommand command) {
        log.info("Turn received for game {}: {} tokens", command.gameId(),
                command.turn().getTakeTokens());

        GameState game = getById(command.gameId());

        TurnValidationResult result = GameRules.validateTurn(game, command.turn());
        if (!result.valid()) {
            throw new InvalidTurnException(result.reason());
        }

        applyTurnAndCheckWinCondition(game, command);

        applyNextComputerTurn(game);
    }

    /**
     * Applies a valid turn to the game state and updates the game accordingly: - If the last token
     * was taken, the game ends and the winner is determined. - Otherwise, it becomes the next
     * player's turn.
     *
     * @param game The current game state
     * @param command The turn that was just played
     */
    private void applyTurnAndCheckWinCondition(GameState game, ExecuteTurnCommand command) {
        // apply turn to game state
        game.applyTurn(command.turn());

        // check if game has ended and compute winner
        if (game.hasGameEnded()) {
            game.setWinner(GameRules.getWinner(game));
            game.setNextTurn(null);

            log.info("Game {} ended. Winner: {}", game.getUuid(), game.getWinner());

        } else {
            // game continues with next turn
            game.setNextTurn(GameRules.getNextTurnPlayer(game));
        }
    }

    /**
     * Automatically performs the computer's turn if it's the computer's move and the game hasn't
     * ended. If the computer's move results in the game ending, the winner is updated accordingly.
     *
     * @param game The current game state
     */
    private void applyNextComputerTurn(GameState game) {
        if (game.hasGameEnded()) {
            return;
        }
        if (game.getNextTurn() != PlayerType.COMPUTER) {
            return;
        }

        Turn turn = computeTurn.computeTurn(game);
        assert (GameRules.validateTurn(game, turn).valid());
        applyTurnAndCheckWinCondition(game, new ExecuteTurnCommand(game.getUuid(), turn));
    }

}
