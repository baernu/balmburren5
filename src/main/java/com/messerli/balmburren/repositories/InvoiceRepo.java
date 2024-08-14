package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findById(Long id);
    boolean existsById(Long id);

}
