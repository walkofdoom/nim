package hamburg.foo.nim.game.adapter.in.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import hamburg.foo.nim.game.domain.model.Turn;

@Mapper
public interface TurnMapper {

    TurnMapper INSTANCE = Mappers.getMapper(TurnMapper.class);

    Turn mapDTOtoTurn(TurnRequest turnRequest);

}
