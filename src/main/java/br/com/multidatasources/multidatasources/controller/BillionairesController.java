package br.com.multidatasources.multidatasources.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.multidatasources.multidatasources.model.Billionaires;
import br.com.multidatasources.multidatasources.service.BillionairesService;

@RestController
@RequestMapping("/billionaires")
public class BillionairesController {
    
    private final BillionairesService billionairesService;

    public BillionairesController(BillionairesService billionairesService) {
        this.billionairesService = billionairesService;
    }
    
    @GetMapping
    public List<Billionaires> findAllMaster() {
        return billionairesService.findAll();
    }
    
    @PostMapping
    public Billionaires saveMaster(@RequestBody Billionaires billionaires) {
        return billionairesService.save(billionaires);
    }

}
