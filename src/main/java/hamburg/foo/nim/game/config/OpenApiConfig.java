package hamburg.foo.nim.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI nimApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nim Game API")
                        .version("1.0.0")
                        .description("""
                                This API allows playing a Nim game against a computer player (Misère variant).

                                **How it works:**
                                1. Use `POST /v1/games/create` to start a new game. Optionally choose who starts.
                                2. Submit turns using `POST /v1/games/{id}/turn` with the number of tokens (1–3).
                                3. The game ends when the last token is taken. The player who takes it **loses**.
                                4. Use `GET /v1/games/{id}` anytime to check the current game state.
                                """)
                        .contact(new Contact()
                                .email("n8jswsyz@foo.hamburg")
                                .url("https://github.com/walkofdoom/nim"))
                        .license(new License()
                                .name("CC BY-NC 4.0")
                                .url("https://creativecommons.org/licenses/by-nc/4.0/")));
    }
}
