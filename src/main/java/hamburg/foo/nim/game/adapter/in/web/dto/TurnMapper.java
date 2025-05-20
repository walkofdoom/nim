package hamburg.foo.nim.game.adapter.in.web.dto;

import org.mapstruct.Mapper;

import hamburg.foo.nim.game.domain.model.Turn;

@Mapper(componentModel = "spring")
public interface TurnMapper {

    Turn mapDTOtoTurn(TurnRequest turnRequest);

}
