package hamburg.foo.nim.game.adapter.in.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import hamburg.foo.nim.game.domain.model.GameState;

@Mapper
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    @Mapping(target = "id", source = "uuid")
    GameResponse mapGameToDto(GameState game);

    @org.mapstruct.Named("mapDtoToPlayerType")
    hamburg.foo.nim.game.domain.model.PlayerType mapDtoToPlayerType(PlayerType playerType);
}
