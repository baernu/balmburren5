package com.messerli.balmburren.services.serviceImpl;


import com.messerli.balmburren.entities.Invoice;
import com.messerli.balmburren.entities.Reference;
import com.messerli.balmburren.repositories.InvoiceRepo;
import com.messerli.balmburren.repositories.ReferenceRepo;
import com.messerli.balmburren.services.InvoiceService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepo invoiceRepo;
    private final ReferenceRepo referenceRepo;

    public InvoiceServiceImpl(InvoiceRepo invoiceRepo, ReferenceRepo referenceRepo) {
        this.invoiceRepo = invoiceRepo;
        this.referenceRepo = referenceRepo;
    }

    @Override
    public Optional<Invoice> saveInvoice(Invoice invoice) {
        log.info("Saving new Invoice: {}", invoice);
        return Optional.of(invoiceRepo.save(invoice));
    }

    @Override
    public Optional<Invoice> putInvoice(Invoice invoice) {
        log.info("Updating Invoice: {}", invoice);
        return Optional.of(invoiceRepo.save(invoice));
    }

    @Override
    public Optional<Invoice> getInvoice(Long id) {
        Optional<Invoice> invoice = invoiceRepo.findById(id);
        log.info("Get Invoice: {}", invoice);
        return invoice;
    }

    @Override
    public Optional<Invoice> deleteInvoice(Invoice invoice) {
        log.info("Deleted Invoice: {}", invoice);
        invoiceRepo.delete(invoice);
        return Optional.of(invoice);
    }

    @Override
    public boolean existInvoice(Long id) {
        boolean bool = invoiceRepo.existsById(id);
        log.info("Invoice exist: {}", bool);
        return bool;
    }

    @Override
    public Optional<Reference> findByName(String name) {
        Optional<Reference> reference = referenceRepo.findByName(name);
        log.info("Reference: {}", reference.get());
        return reference;
    }


    @Override
    public Optional<Reference> createReference(Reference reference) {
        log.info("Saving new Reference: {}", reference);
        return Optional.of(referenceRepo.save(reference));
    }
}
