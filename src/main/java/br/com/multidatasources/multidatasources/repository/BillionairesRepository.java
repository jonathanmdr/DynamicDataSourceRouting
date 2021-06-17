package br.com.multidatasources.multidatasources.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.multidatasources.multidatasources.model.Billionaires;

@Repository
public interface BillionairesRepository extends JpaRepository<Billionaires, Long> {

}
