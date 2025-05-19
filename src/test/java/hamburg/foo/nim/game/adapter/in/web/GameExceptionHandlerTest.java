package hamburg.foo.nim.game.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import hamburg.foo.nim.game.adapter.in.web.dto.GameMapperImpl;
import hamburg.foo.nim.game.adapter.in.web.dto.TurnMapperImpl;
import hamburg.foo.nim.game.application.create.CreateGameUseCase;
import hamburg.foo.nim.game.application.query.GetGameUseCase;
import hamburg.foo.nim.game.application.turn.ExecuteTurnCommand;
import hamburg.foo.nim.game.application.turn.ExecuteTurnUseCase;
import hamburg.foo.nim.game.domain.exception.GameNotFoundException;
import hamburg.foo.nim.game.domain.exception.InvalidTurnException;

@WebMvcTest(GameController.class)
@Import({ GameMapperImpl.class, TurnMapperImpl.class })
class GameExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetGameUseCase getGame;

    @MockitoBean
    private CreateGameUseCase createGame;

    @MockitoBean
    private ExecuteTurnUseCase executeTurn;

    @Test
    void invalidTurnReturnsBadRequest() throws Exception {
        InvalidTurnException exception = new InvalidTurnException("invalid");

        doThrow(exception).when(executeTurn).executeTurn(any(ExecuteTurnCommand.class));

        mockMvc.perform(post("/v1/games/789/turn")
                .contentType("application/json")
                .content("""
                        {
                            "takeTokens": 2
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidGameIdReturnsNotFound() throws Exception {
        when(getGame.getById("555")).thenThrow(new GameNotFoundException("555"));

        mockMvc.perform(post("/v1/games/555/turn")
                .contentType("application/json")
                .content("""
                        {
                            "takeTokens": 2
                        }
                        """))
                .andExpect(status().isNotFound());
    }
}
