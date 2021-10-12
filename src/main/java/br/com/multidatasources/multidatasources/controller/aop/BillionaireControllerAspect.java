package br.com.multidatasources.multidatasources.controller.aop;

import br.com.multidatasources.multidatasources.config.aop.logexecution.LogExecutionTime;
import br.com.multidatasources.multidatasources.controller.dto.BillionaireInputDto;
import br.com.multidatasources.multidatasources.controller.dto.BillionaireOutputDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BillionaireControllerAspect {

    @LogExecutionTime
    ResponseEntity<List<BillionaireOutputDto>> findAll();

    @LogExecutionTime
    ResponseEntity<BillionaireOutputDto> findById(Long id);

    @LogExecutionTime
    ResponseEntity<BillionaireOutputDto> save(BillionaireInputDto billionairesInputDto);

    @LogExecutionTime
    ResponseEntity<BillionaireOutputDto> update(Long id, BillionaireInputDto billionairesInputDto);

    @LogExecutionTime
    void delete(Long id);

}
