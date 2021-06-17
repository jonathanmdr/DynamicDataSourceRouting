package br.com.multidatasources.multidatasources.controller.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.multidatasources.multidatasources.controller.dto.BillionairesInputDto;
import br.com.multidatasources.multidatasources.controller.dto.BillionairesOutputDto;
import br.com.multidatasources.multidatasources.model.Billionaires;

@Component
public class BillionairesMapper {

    private final ModelMapper modelMapper;

    public BillionairesMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Billionaires toModel(BillionairesInputDto inputDto) {
        return modelMapper.map(inputDto, Billionaires.class);
    }

    public BillionairesOutputDto toDto(Billionaires model) {
        return modelMapper.map(model, BillionairesOutputDto.class);
    }

    public List<BillionairesOutputDto> toCollectionDto(List<Billionaires> modelList) {
        return modelList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
