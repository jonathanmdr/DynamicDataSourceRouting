package br.com.multidatasources.controller.mapper;

import br.com.multidatasources.controller.dto.BillionaireInputDto;
import br.com.multidatasources.controller.dto.BillionaireOutputDto;
import br.com.multidatasources.model.Billionaire;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillionaireMapper {

    private final ModelMapper modelMapper;

    public BillionaireMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Billionaire toModel(BillionaireInputDto inputDto) {
        return modelMapper.map(inputDto, Billionaire.class);
    }

    public BillionaireOutputDto toDto(Billionaire model) {
        return modelMapper.map(model, BillionaireOutputDto.class);
    }

    public List<BillionaireOutputDto> toCollectionDto(List<Billionaire> modelList) {
        return modelList.stream()
                .map(this::toDto)
                .toList();
    }

}
