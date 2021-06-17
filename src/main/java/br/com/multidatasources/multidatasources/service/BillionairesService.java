package br.com.multidatasources.multidatasources.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.multidatasources.multidatasources.model.Billionaires;
import br.com.multidatasources.multidatasources.repository.BillionairesRepository;

@Service
@Transactional
public class BillionairesService {

    private final BillionairesRepository billionairesRepository;

    public BillionairesService(BillionairesRepository billionairesRepository) {
        this.billionairesRepository = billionairesRepository;
    }

    @Transactional(readOnly = true)
    public Billionaires findById(Long id) {
        return billionairesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Register with id %s not found", id)));
    }

    @Transactional(readOnly = true)
    public List<Billionaires> findAll() {
        return billionairesRepository.findAll();
    }

    public Billionaires save(Billionaires billionaires) {
        return billionairesRepository.save(billionaires);
    }

    public void delete(Billionaires billionaires) {
        billionairesRepository.delete(billionaires);
    }

}
