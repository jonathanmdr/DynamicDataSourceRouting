package br.com.multidatasources.multidatasources.controller;

import java.util.List;

import javax.validation.Valid;

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

import br.com.multidatasources.multidatasources.controller.dto.BillionairesInputDto;
import br.com.multidatasources.multidatasources.controller.dto.BillionairesOutputDto;
import br.com.multidatasources.multidatasources.controller.mapper.BillionairesMapper;
import br.com.multidatasources.multidatasources.controller.utils.ResourceUriHelper;
import br.com.multidatasources.multidatasources.model.Billionaires;
import br.com.multidatasources.multidatasources.service.BillionairesService;

@RestController
@RequestMapping("/billionaires")
public class BillionairesController {

    private final BillionairesService billionairesService;
    private final BillionairesMapper billionairesMapper;

    public BillionairesController(BillionairesService billionairesService, BillionairesMapper billionairesMapper) {
        this.billionairesService = billionairesService;
        this.billionairesMapper = billionairesMapper;
    }

    @GetMapping
    public ResponseEntity<List<BillionairesOutputDto>> findAll() {
        List<BillionairesOutputDto> responseBody = billionairesMapper.toCollectionDto(billionairesService.findAll());
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillionairesOutputDto> findById(@PathVariable Long id) {
        BillionairesOutputDto responseBody = billionairesMapper.toDto(billionairesService.findById(id));
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<BillionairesOutputDto> save(@Valid @RequestBody BillionairesInputDto billionairesInputDto) {
        Billionaires billionaires = billionairesMapper.toModel(billionairesInputDto);
        BillionairesOutputDto responseBody = billionairesMapper.toDto(billionairesService.save(billionaires));
        return ResponseEntity.created(ResourceUriHelper.getUri(responseBody.getId())).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillionairesOutputDto> update(@PathVariable Long id, @Valid @RequestBody BillionairesInputDto billionairesInputDto) {
        Billionaires billionaires = billionairesService.findById(id);

        BeanUtils.copyProperties(billionairesInputDto, billionaires, "id");

        BillionairesOutputDto responseBody = billionairesMapper.toDto(billionairesService.save(billionaires));
        return ResponseEntity.ok(responseBody);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Billionaires billionaires = billionairesService.findById(id);
        billionairesService.delete(billionaires);
    }

}
