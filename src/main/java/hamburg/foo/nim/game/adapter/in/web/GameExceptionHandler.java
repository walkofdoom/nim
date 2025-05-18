package hamburg.foo.nim.game.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hamburg.foo.nim.game.domain.exception.GameNotFoundException;
import hamburg.foo.nim.game.domain.exception.InvalidTurnException;

@ControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<String> handleNotFound(GameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTurnException.class)
    public ResponseEntity<String> handleInvalidTurn(InvalidTurnException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
