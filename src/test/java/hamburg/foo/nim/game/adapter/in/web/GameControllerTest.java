package hamburg.foo.nim.game.adapter.in.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import hamburg.foo.nim.game.application.turn.ExecuteTurnUseCase;
import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;

@WebMvcTest(GameController.class)
@Import({ GameMapperImpl.class, TurnMapperImpl.class })
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetGameUseCase getGame;

    @MockitoBean
    private CreateGameUseCase createGame;

    @MockitoBean
    private ExecuteTurnUseCase executeTurn;

    @Test
    void getGameReturnsGameResponse() throws Exception {
        GameState mockGame = GameState.builder()
                .uuid("123")
                .nextTurn(PlayerType.HUMAN)
                .winner(null)
                .remainingTokens(5)
                .build();

        when(getGame.getById("123")).thenReturn(mockGame);

        mockMvc.perform(get("/v1/games/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.nextTurn").value("HUMAN"))
                .andExpect(jsonPath("$.remainingTokens").value(5));
    }

    @Test
    void createGameWithRequestBody() throws Exception {
        GameState game = GameState.builder().uuid("456").nextTurn(PlayerType.HUMAN).remainingTokens(10).build();
        when(createGame.createNewGame(PlayerType.HUMAN)).thenReturn(game);

        mockMvc.perform(post("/v1/games/create")
                .contentType("application/json")
                .content("""
                        {
                            "startingPlayer": "HUMAN"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("456"))
                .andExpect(jsonPath("$.nextTurn").value("HUMAN"))
                .andExpect(jsonPath("$.remainingTokens").value(10));
    }

    @Test
    void executeTurnReturnsGameResponse() throws Exception {
        GameState updatedGame = GameState.builder().uuid("789").nextTurn(PlayerType.HUMAN).remainingTokens(3).build();
        when(getGame.getById("789")).thenReturn(updatedGame);

        mockMvc.perform(post("/v1/games/789/turn")
                .contentType("application/json")
                .content("""
                        {
                            "takeTokens": 2
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("789"))
                .andExpect(jsonPath("$.nextTurn").value("HUMAN"))
                .andExpect(jsonPath("$.remainingTokens").value(3));
    }

}
