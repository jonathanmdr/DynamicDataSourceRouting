package br.com.multidatasources.controller.mapper;

import br.com.multidatasources.controller.dto.BillionaireInputDto;
import br.com.multidatasources.controller.dto.BillionaireOutputDto;
import br.com.multidatasources.model.Billionaire;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillionaireMapper {

    Billionaire toModel(BillionaireInputDto inputDto);
    BillionaireOutputDto toDto(Billionaire model);
    List<BillionaireOutputDto> toCollectionDto(List<Billionaire> modelList);

}
