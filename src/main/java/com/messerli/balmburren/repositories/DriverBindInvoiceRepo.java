package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.DriverBindInvoice;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverBindInvoiceRepo extends JpaRepository<DriverBindInvoice, Long> {
    Optional<DriverBindInvoice> findByDateFrom_DateAndDateTo_DateAndPersonInvoice(String dateFrom, String dateTo, User peopleInvoice);
    boolean existsByDateFrom_DateAndDateTo_DateAndPersonInvoice(String dateFrom, String dateTo, User peopleInvoice);
    Optional<List<DriverBindInvoice>> getAllByPersonInvoice(User user);
    Optional<List<DriverBindInvoice>> getAllByDateFrom_Date(String dateFrom);

}