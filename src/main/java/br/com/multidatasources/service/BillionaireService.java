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

    public BillionaireService(final BillionaireRepository billionairesRepository) {
        this.billionaireRepository = billionairesRepository;
    }

    @Transactional(readOnly = true)
    public Billionaire findById(final Long id) {
        return billionaireRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Register with id %s not found", id)));
    }

    @Transactional(readOnly = true)
    public List<Billionaire> findAll() {
        return billionaireRepository.findAll();
    }

    public Billionaire save(final Billionaire billionaire) {
        return billionaireRepository.save(billionaire);
    }

    public void delete(final Billionaire billionaire) {
        billionaireRepository.delete(billionaire);
    }

}
