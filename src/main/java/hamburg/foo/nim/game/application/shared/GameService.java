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
        return sessionRepository.find(uuid)
                .orElseThrow(() -> new GameNotFoundException(uuid));
    }

    @Override
    public GameState createNewGame(PlayerType startingPlayer) {
        if (startingPlayer == null) {
            startingPlayer = this.defaultStartingPlayer;
        }
        GameState game = gameStateFactory.createNewGame(startingTokens, startingPlayer);
        sessionRepository.save(game);
        applyNextComputerTurn(game);
        return game;
    }

    @Override
    public void executeTurn(ExecuteTurnCommand command) {
        GameState game = getById(command.gameId());

        TurnValidationResult result = GameRules.validateTurn(game, command.turn());
        if (!result.valid()) {
            throw new InvalidTurnException(result.reason());
        }

        applyTurnAndCheckWinCondition(game, command);

        applyNextComputerTurn(game);
    }

    private void applyTurnAndCheckWinCondition(GameState game, ExecuteTurnCommand command) {
        // apply turn to game state
        game.applyTurn(command.turn());

        // check if game has ended and compute winner
        if (game.hasGameEnded()) {
            game.setWinner(GameRules.getWinner(game));
            game.setNextTurn(null);
        } else {
            // game continues with next turn
            game.setNextTurn(GameRules.getNextTurnPlayer(game));
        }
    }

    /**
     * Computer is allowed to determine it's next turn and the turn is executed. If
     * the game has ended or it's the player's turn, no action is taken.
     * 
     * @param game Current game state.
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
