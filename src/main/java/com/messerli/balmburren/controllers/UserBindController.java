package com.messerli.balmburren.controllers;


import com.messerli.balmburren.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/users/bind/")
@RestController
public class UserBindController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserBindService userBindService;
    @Autowired
    private TourService tourService;
    @Autowired
    private DatesService datesService;

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/address")
    ResponseEntity<Address> createAddress(@RequestBody Address address) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/address").toUriString());
        return ResponseEntity.created(uri).body(userBindService.saveAddress(address));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/address")
    ResponseEntity<Address> putAddress(@RequestBody Address address) {
        Optional<Address> address1 = userBindService.getAddress(address.getId());
        if (!address1.isPresent()) throw new NoSuchElementFoundException("Address not found");
        return ResponseEntity.ok().body(userBindService.putAddress(address));}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/address/{id}")
    ResponseEntity<Address> deleteAddress(@PathVariable("id") Long id) {
        Optional<Address> address1 = userBindService.getAddress(id);
        if (!address1.isPresent()) throw new NoSuchElementFoundException("Address not found");
        return ResponseEntity.ok().body(userBindService.deleteAddress(address1.get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/address/{id}")
    ResponseEntity<Address> getAddress(@PathVariable("id") Long id) {
        Optional<Address> address = userBindService.getAddress(id);
        if (!address.isPresent()) throw new NoSuchElementFoundException("Address not found");
        else {
            return ResponseEntity.ok().body(address.get());}
        }


    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/person/bind/deliveraddress")
    ResponseEntity<PersonBindDeliverAddress> createPersonBindDeliverAddress(@RequestBody PersonBindDeliverAddress personBindDeliverAddress) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/deliveraddress").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindDeliverAddress(personBindDeliverAddress));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/person/bind/deliveraddress")
    ResponseEntity<PersonBindDeliverAddress> putPersonBindDeliverAddress(@RequestBody PersonBindDeliverAddress personBindDeliverAddress) {
        PersonBindDeliverAddress personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(personBindDeliverAddress.getPerson());
        if (personBindDeliverAddress1 == null) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(userBindService.putPersonBindDeliverAddress(personBindDeliverAddress));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/deliveraddress/{username}")
    ResponseEntity<PersonBindDeliverAddress> getPersonBindDeliverAddress(@PathVariable("username") String username) {
        PersonBindDeliverAddress personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(getPeople(username));
        if (personBindDeliverAddress1 == null) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(personBindDeliverAddress1);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/deliveraddress/exist/{username}")
    ResponseEntity<Boolean> existPersonBindDeliverAddress(@PathVariable("username") String username) {
        boolean bool = userBindService.existPersonBindDeliverAddress(getPeople(username));
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/person/bind/deliveraddress/{people}")
    ResponseEntity<PersonBindDeliverAddress> deletePersonBindDeliverAddress(@PathVariable("username") String username) {
        PersonBindDeliverAddress personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(getPeople(username));
        if (personBindDeliverAddress1 == null) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(userBindService.deletePersonBindDeliverAddress(personBindDeliverAddress1.getPerson()));}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/person/bind/invoice")
    ResponseEntity<PersonBindInvoice> createPersonBindInvoice(@RequestBody PersonBindInvoice personBindInvoice) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/invoice").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindInvoice(personBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/person/bind/invoice")
    ResponseEntity<PersonBindInvoice> putPersonBindInvoice(@RequestBody PersonBindInvoice personBindInvoice) {
        return ResponseEntity.ok().body(userBindService.putPersonBindInvoice(personBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<PersonBindInvoice> getPersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                           @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        PersonBindInvoice personBindInvoice1 = userBindService.getPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice), getPeople(deliver));
        if (personBindInvoice1 == null) throw new NoSuchElementFoundException("PersonBindInvoice not found");
        return ResponseEntity.ok().body(personBindInvoice1);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/exist/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<Boolean> existPersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                           @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        boolean bool = userBindService.existPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice), getPeople(deliver));
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/person/bind/invoice/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<PersonBindInvoice> deletePersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                              @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        PersonBindInvoice personBindInvoice1 = userBindService.getPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice), getPeople(deliver));
        if (personBindInvoice1 == null) throw new NoSuchElementFoundException("PersonBindInvoice not found");
        return ResponseEntity.ok().body(userBindService.deletePersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice), getPeople(deliver)));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/deliver/{username}")
    ResponseEntity<List<PersonBindInvoice>> getAllPersonBindInvoiceForDeliver(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForDeliver(getPeople(username)));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/invoice/{username}")
    ResponseEntity<List<PersonBindInvoice>> getAllPersonBindInvoiceForInvoice(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForInvoice(getPeople(username)));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice")
    ResponseEntity<List<PersonBindInvoice>> getAllPersonBindInvoice() {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoice());}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/{dateFrom}")
    ResponseEntity<List<PersonBindInvoice>> getAllPersonBindInvoiceDateFrom(@PathVariable("dateFrom") String dateFrom) {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForDateFrom(dateFrom));}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/person/bind/phone")
    ResponseEntity<PersonBindPhone> createPersonBindPhone(@RequestBody PersonBindPhone personBindPhone) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/phone").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindPhone(personBindPhone));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/person/bind/phone")
    ResponseEntity<PersonBindPhone> putPersonBindPhone(@RequestBody PersonBindPhone personBindPhone) {
        PersonBindPhone personBindPhone1 = userBindService.getPersonBindPhone(personBindPhone.getPerson());
        if (personBindPhone1 == null) throw new NoSuchElementFoundException("PersonBindPhone not found");
        return ResponseEntity.ok().body(userBindService.putPersonBindPhone(personBindPhone));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/phone/{username}")
    ResponseEntity<PersonBindPhone> getPersonBindPhone(@PathVariable("username") String username) {
        PersonBindPhone personBindPhone = userBindService.getPersonBindPhone(getPeople(username));
        if (personBindPhone == null) throw new NoSuchElementFoundException("PersonBindPhone not found");
        return ResponseEntity.ok().body(personBindPhone);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/phone/exist/{username}")
    ResponseEntity<Boolean> existPersonBindPhone(@PathVariable("username") String username) {
        boolean bool = userBindService.existPersonBindPhone(getPeople(username));
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/person/bind/phone/{username}")
    ResponseEntity<PersonBindPhone> deletePersonBindPhone(@PathVariable("username") String username) {
        PersonBindPhone personBindPhone = userBindService.getPersonBindPhone(getPeople(username));
        if (personBindPhone == null) throw new NoSuchElementFoundException("PersonBindPhone not found");
        return ResponseEntity.ok().body(userBindService.deletePersonBindPhone(personBindPhone.getPerson()));}



    private People getPeople(String username) {
        People people = userService.getUser(username);
        if (people == null) throw new NoSuchElementFoundException("Person not found");
        return people;
    }
    private String getDates(Long date) {
        Dates dates = datesService.getDate(date);
        if (dates == null) throw new NoSuchElementFoundException("Dates not found");
        return dates.getDate();
    }
    private Tour getTour(String number) {
        if (tourService.existTour(number)) {
            return tourService.getTour(number);
        } else {
            throw new NoSuchElementFoundException("Tour not found");
        }
    }

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/tour/{username}/{tour}")
    ResponseEntity<PersonBindTour> getPersonBindTour(@PathVariable("tour") String tour, @PathVariable("username") String username) {
        PersonBindTour personBindTour = userBindService.getPersonBindTour(getPeople(username), getTour(tour));
        return ResponseEntity.ok().body(personBindTour);}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/person/bind/tour")
    ResponseEntity<PersonBindTour> createPersonBindTour(@RequestBody PersonBindTour personBindTour) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/tour").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindTour(personBindTour));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/person/bind/tour")
    ResponseEntity<PersonBindTour> updatePersonBindTour(@RequestBody PersonBindTour personBindTour) {
        return ResponseEntity.ok().body(userBindService.savePersonBindTour(personBindTour));}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/person/bind/tour/{username}/{tour}")
    ResponseEntity<PersonBindTour> deletePersonBindTour(@PathVariable("tour") String tour, @PathVariable("username") String username) {
        PersonBindTour personBindTour = userBindService.getPersonBindTour(getPeople(username), getTour(tour));
        userBindService.deletePersonBindTour(personBindTour);
        return ResponseEntity.ok().body(personBindTour);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/tour/{tour}")
    ResponseEntity<List<PersonBindTour>> getAllPersonsForTour(@PathVariable("tour") String tour) {
        if (tourService.existTour(tour)) {
            return ResponseEntity.ok().body(userBindService.getAllPersonsForTour(getTour(tour)));
        }
        else { throw new NoSuchElementFoundException("Tour not found"); }}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping ("/person/bind/tour/exist/{username}/{tour}")
    ResponseEntity<Boolean> existTourForPerson(@PathVariable("username") String username, @PathVariable("tour") String tour) {
        boolean bool = userBindService.existPersonAndTour(getPeople(username), getTour(tour));
        return ResponseEntity.ok().body(bool);}

}
