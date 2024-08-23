package com.messerli.balmburren.services;


import com.messerli.balmburren.entities.Invoice;
import com.messerli.balmburren.entities.Reference;

import java.util.Optional;

public interface InvoiceService {
    Optional<Invoice> saveInvoice(Invoice invoice);
    Optional<Invoice> putInvoice(Invoice invoice);
    Optional<Invoice> getInvoice(Long id);
    Optional<Invoice> deleteInvoice(Invoice invoice);
    boolean existInvoice(Long id);
    Optional<Reference> findByVal(Long val);
    Optional<Reference> createReference(Reference reference);
}
