package br.com.multidatasources.controller;

import br.com.multidatasources.config.aop.LogExecutionTime;
import br.com.multidatasources.controller.dto.BillionaireInputDto;
import br.com.multidatasources.controller.dto.BillionaireOutputDto;
import br.com.multidatasources.controller.mapper.BillionaireMapper;
import br.com.multidatasources.controller.utils.ResourceUriHelper;
import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.service.BillionaireService;
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

import javax.validation.Valid;
import java.util.List;

@LogExecutionTime
@RestController
@RequestMapping("/billionaires")
public class BillionaireController {

    private final BillionaireService billionaireService;
    private final BillionaireMapper billionaireMapper;

    public BillionaireController(final BillionaireService billionaireService, final BillionaireMapper billionaireMapper) {
        this.billionaireService = billionaireService;
        this.billionaireMapper = billionaireMapper;
    }

    @GetMapping
    public ResponseEntity<List<BillionaireOutputDto>> findAll() {
        List<BillionaireOutputDto> responseBody = billionaireMapper.toCollectionDto(billionaireService.findAll());
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillionaireOutputDto> findById(@PathVariable final Long id) {
        BillionaireOutputDto responseBody = billionaireMapper.toDto(billionaireService.findById(id));
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<BillionaireOutputDto> save(@Valid @RequestBody final BillionaireInputDto billionairesInputDto) {
        Billionaire billionaires = billionaireMapper.toModel(billionairesInputDto);
        BillionaireOutputDto responseBody = billionaireMapper.toDto(billionaireService.save(billionaires));
        return ResponseEntity.created(ResourceUriHelper.getUri(responseBody.getId())).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillionaireOutputDto> update(@PathVariable final Long id, @Valid @RequestBody final BillionaireInputDto billionairesInputDto) {
        Billionaire billionaires = billionaireService.findById(id);

        BeanUtils.copyProperties(billionairesInputDto, billionaires, "id");

        BillionaireOutputDto responseBody = billionaireMapper.toDto(billionaireService.save(billionaires));
        return ResponseEntity.ok(responseBody);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        Billionaire billionaires = billionaireService.findById(id);
        billionaireService.delete(billionaires);
    }

}
