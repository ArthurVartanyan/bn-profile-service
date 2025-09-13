package ru.bank.cosmo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank.cosmo.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}