package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.PersonBindInvoice;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonBindInvoiceRepo extends JpaRepository<PersonBindInvoice, Long> {
    Optional<PersonBindInvoice> findByDateFrom_DateAndDateTo_DateAndPersonInvoiceAndPersonDeliver(String dateFrom, String dateTo, User peopleInvoice, User peopleDeliver);
    boolean existsByDateFrom_DateAndDateTo_DateAndPersonInvoiceAndPersonDeliver(String dateFrom, String dateTo, User peopleInvoice, User peopleDeliver);
    Optional<List<PersonBindInvoice>> getAllByPersonDeliver(User user);
    Optional<List<PersonBindInvoice>> getAllByPersonInvoice(User user);
    Optional<List<PersonBindInvoice>> getAllByDateFrom_Date(String dateFrom);
    Optional<List<PersonBindInvoice>> getAllByDateTo_Date(String dateTo);

}
