package br.com.multidatasources.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.repository.BillionaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BillionaireService {

    private final BillionaireRepository billionaireRepository;

    public BillionaireService(BillionaireRepository billionairesRepository) {
        this.billionaireRepository = billionairesRepository;
    }

    @Transactional(readOnly = true)
    public Billionaire findById(Long id) {
        return billionaireRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Register with id %s not found", id)));
    }

    @Transactional(readOnly = true)
    public List<Billionaire> findAll() {
        return billionaireRepository.findAll();
    }

    public Billionaire save(Billionaire billionaires) {
        return billionaireRepository.save(billionaires);
    }

    public void delete(Billionaire billionaires) {
        billionaireRepository.delete(billionaires);
    }

}
