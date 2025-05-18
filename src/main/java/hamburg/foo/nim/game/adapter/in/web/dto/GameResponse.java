package hamburg.foo.nim.game.adapter.in.web.dto;

public record GameResponse(String id, PlayerType nextTurn, PlayerType winner, int remainingTokens) {

}
