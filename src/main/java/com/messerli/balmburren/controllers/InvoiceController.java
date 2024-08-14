package com.messerli.balmburren.controllers;


import com.messerli.balmburren.entities.Invoice;
import com.messerli.balmburren.exceptions.NoSuchElementFoundException;
import com.messerli.balmburren.services.InvoiceService;
import com.messerli.balmburren.entities.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/ic/")
@RestController
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("invoice")
    ResponseEntity<Optional<Invoice>> createInvoice(@RequestBody Invoice invoice) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/invoice").toUriString());
        return ResponseEntity.created(uri).body(invoiceService.saveInvoice(invoice));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("invoice")
    ResponseEntity<Optional<Invoice>> putInvoice(@RequestBody Invoice invoice) {
        Optional<Invoice> invoice1 = invoiceService.getInvoice(invoice.getId());
        if (invoice1.isEmpty()) throw new NoSuchElementFoundException("Invoice not found");
        return ResponseEntity.ok().body(invoiceService.putInvoice(invoice1.get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("invoice/{id}")
    ResponseEntity<Optional<Invoice>> getInvoice(@PathVariable("id") Long id ) {
        Optional<Invoice> invoice= invoiceService.getInvoice(id);
        if (invoice.isEmpty()) throw new NoSuchElementFoundException("Invoice not found");
        return ResponseEntity.ok().body(invoice);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("invoice/{id}")
    ResponseEntity<Optional<Invoice>> deleteInvoice(@PathVariable("id") Long id ) {
        Optional<Invoice> invoice= invoiceService.getInvoice(id);
        if (invoice.isEmpty()) throw new NoSuchElementFoundException("Invoice not found");
        else return ResponseEntity.ok().body(invoiceService.deleteInvoice(invoice.get()));}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("invoice/exist/{id}")
    ResponseEntity<Boolean> existInvoice(@PathVariable("id") Long id ) {
        return ResponseEntity.ok().body(invoiceService.existInvoice(id));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("invoice/reference/{name}")
    ResponseEntity<Optional<Reference>> getInvoiceReference(@PathVariable("name") String name ) {
        Optional<Reference> reference = invoiceService.findByName(name);
        if(reference.isEmpty()) throw new NoSuchElementFoundException("Reference not found");
        return ResponseEntity.ok().body(reference);}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("invoice/reference")
    ResponseEntity<Optional<Reference>> postInvoiceReference(@RequestBody Reference reference ) {
        Optional<Reference> reference1 = invoiceService.createReference(reference);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/invoice/reference").toUriString());
        return ResponseEntity.created(uri).body(reference1);}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("invoice/reference")
    ResponseEntity<Optional<Reference>> putInvoiceReference(@RequestBody Reference reference ) {
        Optional<Reference> reference1 = invoiceService.createReference(reference);
        return ResponseEntity.ok().body(reference1);}
}
