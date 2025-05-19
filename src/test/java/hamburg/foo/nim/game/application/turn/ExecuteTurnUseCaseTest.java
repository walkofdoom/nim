package hamburg.foo.nim.game.application.turn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import hamburg.foo.nim.game.adapter.out.persistence.GameSessionRepository;
import hamburg.foo.nim.game.application.shared.GameService;
import hamburg.foo.nim.game.domain.exception.InvalidTurnException;
import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;
import hamburg.foo.nim.game.domain.model.Turn;

@ExtendWith(MockitoExtension.class)
class ExecuteTurnUseCaseTest {

    @Mock
    GameSessionRepository repository;

    @Mock
    ComputeTurnUseCase computeTurn;

    @InjectMocks
    GameService service;

    @BeforeEach
    void setupDefaults() {
        ReflectionTestUtils.setField(service, "startingTokens", 13);
        ReflectionTestUtils.setField(service, "defaultStartingPlayer", PlayerType.HUMAN);
    }

    @Test
    void executesValidTurnAndAppliesComputerTurn() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(5).nextTurn(PlayerType.HUMAN).build();
        Turn turn = Turn.builder().takeTokens(2).build();
        ExecuteTurnCommand command = new ExecuteTurnCommand("game1", turn);

        when(repository.find("game1")).thenReturn(Optional.of(game));
        when(computeTurn.computeTurn(any())).thenReturn(Turn.builder().takeTokens(1).build());

        service.executeTurn(command);

        assertEquals(2, game.getRemainingTokens());
        assertEquals(PlayerType.HUMAN, game.getNextTurn());
    }

    @Test
    void executesEndingTurnAndUpdatesGame() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(3).nextTurn(PlayerType.HUMAN).build();
        Turn turn = Turn.builder().takeTokens(3).build();
        ExecuteTurnCommand command = new ExecuteTurnCommand("game1", turn);

        when(repository.find("game1")).thenReturn(Optional.of(game));

        service.executeTurn(command);

        assertEquals(0, game.getRemainingTokens());
        assertNull(game.getNextTurn());
        assertEquals(PlayerType.COMPUTER, game.getWinner());
    }

    @Test
    void throwsIfTurnInvalid() {
        GameState game = GameState.builder().uuid("game1").remainingTokens(1).nextTurn(PlayerType.HUMAN).build();
        Turn turn = Turn.builder().takeTokens(3).build();
        ExecuteTurnCommand command = new ExecuteTurnCommand("game1", turn);

        when(repository.find("game1")).thenReturn(Optional.of(game));

        assertThrows(InvalidTurnException.class, () -> service.executeTurn(command));
    }
}

