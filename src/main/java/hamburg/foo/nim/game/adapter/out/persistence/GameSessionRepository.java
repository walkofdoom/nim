package hamburg.foo.nim.game.adapter.out.persistence;

import java.util.Optional;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import hamburg.foo.nim.game.domain.model.GameState;

@Component
public class GameSessionRepository {

    private final Cache gamesCache;

    public GameSessionRepository(CacheManager cacheManager) {
        this.gamesCache = cacheManager.getCache("games");
    }

    public void save(GameState game) {
        gamesCache.put(game.getUuid(), game);
    }

    public Optional<GameState> find(String id) {
        return Optional.ofNullable(gamesCache.get(id, GameState.class));
    }

    public void remove(String id) {
        gamesCache.evict(id);
    }

}

