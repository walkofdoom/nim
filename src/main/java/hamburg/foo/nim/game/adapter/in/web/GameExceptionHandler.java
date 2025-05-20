package hamburg.foo.nim.game.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hamburg.foo.nim.game.domain.exception.GameNotFoundException;
import hamburg.foo.nim.game.domain.exception.InvalidTurnException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GameExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<String> handleNotFound(GameNotFoundException ex) {
        log.warn("Game not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTurnException.class)
    public ResponseEntity<String> handleInvalidTurn(InvalidTurnException ex) {
        log.warn("Invalid turn: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
