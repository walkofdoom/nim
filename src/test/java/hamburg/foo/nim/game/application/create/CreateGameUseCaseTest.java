package hamburg.foo.nim.game.application.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import hamburg.foo.nim.game.adapter.out.persistence.GameSessionRepository;
import hamburg.foo.nim.game.application.shared.GameService;
import hamburg.foo.nim.game.application.turn.ComputeTurnUseCase;
import hamburg.foo.nim.game.domain.factory.GameStateFactory;
import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;
import hamburg.foo.nim.game.domain.model.Turn;

@ExtendWith(MockitoExtension.class)
class CreateGameUseCaseTest {

    @Mock
    GameSessionRepository repository;

    @Mock
    GameStateFactory factory;

    @Mock
    ComputeTurnUseCase computeTurn;

    @InjectMocks
    GameService service;

    @Test
    void createsNewGameWithDefaultStartingPlayer() {
        PlayerType defaultStarter = PlayerType.HUMAN;
        service = new GameService(factory, computeTurn, repository);
        ReflectionTestUtils.setField(service, "startingTokens", 13);
        ReflectionTestUtils.setField(service, "defaultStartingPlayer", PlayerType.HUMAN);

        GameState mockGame = GameState.builder().uuid("abc").remainingTokens(13).nextTurn(defaultStarter).build();
        when(factory.createNewGame(13, defaultStarter)).thenReturn(mockGame);

        GameState created = service.createNewGame(null);

        assertEquals("abc", created.getUuid());
        verify(repository).save(mockGame);
        assertEquals(PlayerType.HUMAN, mockGame.getNextTurn());
    }

    @Test
    void createsNewGameWithComputerAndAppliesTurn() {
        PlayerType defaultStarter = PlayerType.HUMAN;
        PlayerType specificStarter = PlayerType.COMPUTER;
        service = new GameService(factory, computeTurn, repository);
        ReflectionTestUtils.setField(service, "startingTokens", 13);
        ReflectionTestUtils.setField(service, "defaultStartingPlayer", defaultStarter);

        GameState mockGame = GameState.builder().uuid("abc").remainingTokens(13).nextTurn(specificStarter).build();
        when(factory.createNewGame(13, specificStarter)).thenReturn(mockGame);
        when(computeTurn.computeTurn(mockGame)).thenReturn(Turn.builder().takeTokens(2).build());

        GameState created = service.createNewGame(specificStarter);

        assertEquals("abc", created.getUuid());
        verify(repository).save(mockGame);
        assertEquals(11, mockGame.getRemainingTokens());
        assertEquals(PlayerType.HUMAN, mockGame.getNextTurn());
    }
}
