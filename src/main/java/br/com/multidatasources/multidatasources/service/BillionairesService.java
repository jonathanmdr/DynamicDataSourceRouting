package br.com.multidatasources.multidatasources.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.multidatasources.multidatasources.model.Billionaires;
import br.com.multidatasources.multidatasources.repository.BillionairesRepository;

@Service
public class BillionairesService {
    
    private final BillionairesRepository billionairesRepository;

    public BillionairesService(BillionairesRepository billionairesRepository) {
        this.billionairesRepository = billionairesRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Billionaires> findAll() {
        return billionairesRepository.findAll();
    }
    
    @Transactional
    public Billionaires save(Billionaires billionaires) {
        return billionairesRepository.save(billionaires);
    }

}
