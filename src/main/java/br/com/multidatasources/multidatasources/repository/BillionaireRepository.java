package br.com.multidatasources.multidatasources.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.multidatasources.multidatasources.model.Billionaire;

@Repository
public interface BillionaireRepository extends JpaRepository<Billionaire, Long> {

}
