package hamburg.foo.nim.game.adapter.out.persistence;

import java.util.Optional;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;

import hamburg.foo.nim.game.domain.model.GameState;

@Component
public class GameSessionRepository {

    private final Cache<String, GameState> gameCache;

    public GameSessionRepository(CacheManager cacheManager) {
        this.gameCache = (Cache<String, GameState>) cacheManager.getCache("games").getNativeCache();
    }

    public void save(GameState game) {
        gameCache.put(game.getUuid(), game);
    }

    public Optional<GameState> find(String id) {
        return Optional.ofNullable(gameCache.getIfPresent(id));
    }

    public void remove(String id) {
        gameCache.invalidate(id);
    }
}

