package br.com.multidatasources.multidatasources.controller;

import java.util.List;

import javax.validation.Valid;

import br.com.multidatasources.multidatasources.config.aop.logexecution.LogExecutionTime;
import br.com.multidatasources.multidatasources.controller.aop.BillionaireControllerAspect;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.multidatasources.multidatasources.controller.dto.BillionaireInputDto;
import br.com.multidatasources.multidatasources.controller.dto.BillionaireOutputDto;
import br.com.multidatasources.multidatasources.controller.mapper.BillionaireMapper;
import br.com.multidatasources.multidatasources.controller.utils.ResourceUriHelper;
import br.com.multidatasources.multidatasources.model.Billionaire;
import br.com.multidatasources.multidatasources.service.BillionaireService;

@RestController
@RequestMapping("/billionaires")
public class BillionaireController implements BillionaireControllerAspect {

    private final BillionaireService billionaireService;
    private final BillionaireMapper billionaireMapper;

    public BillionaireController(BillionaireService billionaireService, BillionaireMapper billionaireMapper) {
        this.billionaireService = billionaireService;
        this.billionaireMapper = billionaireMapper;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<BillionaireOutputDto>> findAll() {
        List<BillionaireOutputDto> responseBody = billionaireMapper.toCollectionDto(billionaireService.findAll());
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<BillionaireOutputDto> findById(@PathVariable Long id) {
        BillionaireOutputDto responseBody = billionaireMapper.toDto(billionaireService.findById(id));
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    @Override
    public ResponseEntity<BillionaireOutputDto> save(@Valid @RequestBody BillionaireInputDto billionairesInputDto) {
        Billionaire billionaires = billionaireMapper.toModel(billionairesInputDto);
        BillionaireOutputDto responseBody = billionaireMapper.toDto(billionaireService.save(billionaires));
        return ResponseEntity.created(ResourceUriHelper.getUri(responseBody.getId())).body(responseBody);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<BillionaireOutputDto> update(@PathVariable Long id, @Valid @RequestBody BillionaireInputDto billionairesInputDto) {
        Billionaire billionaires = billionaireService.findById(id);

        BeanUtils.copyProperties(billionairesInputDto, billionaires, "id");

        BillionaireOutputDto responseBody = billionaireMapper.toDto(billionaireService.save(billionaires));
        return ResponseEntity.ok(responseBody);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable Long id) {
        Billionaire billionaires = billionaireService.findById(id);
        billionaireService.delete(billionaires);
    }

}
