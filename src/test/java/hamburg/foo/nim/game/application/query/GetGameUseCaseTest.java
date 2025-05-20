package hamburg.foo.nim.game.application.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hamburg.foo.nim.game.adapter.out.persistence.GameSessionRepository;
import hamburg.foo.nim.game.application.shared.GameService;
import hamburg.foo.nim.game.domain.exception.GameNotFoundException;
import hamburg.foo.nim.game.domain.model.GameState;

@ExtendWith(MockitoExtension.class)
class GetGameUseCaseTest {

    @Mock
    GameSessionRepository repository;

    @InjectMocks
    GameService service;

    @Test
    void returnsGameById() {
        GameState game = GameState.builder().uuid("123").build();
        when(repository.find("123")).thenReturn(Optional.of(game));

        GameState result = service.getById("123");

        assertEquals("123", result.getUuid());
    }

    @Test
    void throwsIfGameNotFound() {
        when(repository.find("xyz")).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> service.getById("xyz"));
    }
}

