package com.messerli.balmburren.controllers;


import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.exceptions.NoSuchElementFoundException;
import com.messerli.balmburren.services.DatesService;
import com.messerli.balmburren.services.TourService;
import com.messerli.balmburren.services.UserBindService;
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
@RequestMapping("/bd/")
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
    ResponseEntity<Optional<Address>> createAddress(@RequestBody Address address) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/address").toUriString());
        return ResponseEntity.created(uri).body(userBindService.saveAddress(address));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/address")
    ResponseEntity<Optional<Address>> putAddress(@RequestBody Address address) {
        Optional<Address> address1 = userBindService.getAddress(address.getId());
        if (!address1.isPresent()) throw new NoSuchElementFoundException("Address not found");
        return ResponseEntity.ok().body(userBindService.putAddress(address));}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/address/{id}")
    ResponseEntity<Optional<Address>> deleteAddress(@PathVariable("id") Long id) {
        Optional<Address> address1 = userBindService.getAddress(id);
        if (!address1.isPresent()) throw new NoSuchElementFoundException("Address not found");
        return ResponseEntity.ok().body(userBindService.deleteAddress(address1.get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/address/{id}")
    ResponseEntity<Optional<Address>> getAddress(@PathVariable("id") Long id) {
        Optional<Address> address = userBindService.getAddress(id);
        if (!address.isPresent()) throw new NoSuchElementFoundException("Address not found");
//        else {
        return ResponseEntity.ok().body(address);}



    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/person/bind/deliveraddress")
    ResponseEntity<Optional<PersonBindDeliverAddress>> createPersonBindDeliverAddress(@RequestBody PersonBindDeliverAddress personBindDeliverAddress) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/deliveraddress").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindDeliverAddress(personBindDeliverAddress));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/person/bind/deliveraddress")
    ResponseEntity<Optional<PersonBindDeliverAddress>> putPersonBindDeliverAddress(@RequestBody PersonBindDeliverAddress personBindDeliverAddress) {
        Optional<PersonBindDeliverAddress> personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(personBindDeliverAddress.getUser());
        if (personBindDeliverAddress1.isEmpty()) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(userBindService.putPersonBindDeliverAddress(personBindDeliverAddress));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/deliveraddress/{username}")
    ResponseEntity<Optional<PersonBindDeliverAddress>> getPersonBindDeliverAddress(@PathVariable("username") String username) {
        Optional<PersonBindDeliverAddress> personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(getPeople(username).get());
        if (personBindDeliverAddress1.isEmpty()) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(personBindDeliverAddress1);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/deliveraddress/exist/{username}")
    ResponseEntity<Boolean> existPersonBindDeliverAddress(@PathVariable("username") String username) {
        boolean bool = userBindService.existPersonBindDeliverAddress(getPeople(username).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/person/bind/deliveraddress/{people}")
    ResponseEntity<Optional<PersonBindDeliverAddress>> deletePersonBindDeliverAddress(@PathVariable("username") String username) {
        Optional<PersonBindDeliverAddress> personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(getPeople(username).get());
        if (personBindDeliverAddress1.isEmpty()) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(userBindService.deletePersonBindDeliverAddress(personBindDeliverAddress1.get().getUser()));}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/person/bind/invoice")
    ResponseEntity<Optional<PersonBindInvoice>> createPersonBindInvoice(@RequestBody PersonBindInvoice personBindInvoice) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/invoice").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindInvoice(personBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/person/bind/invoice")
    ResponseEntity<Optional<PersonBindInvoice>> putPersonBindInvoice(@RequestBody PersonBindInvoice personBindInvoice) {
        return ResponseEntity.ok().body(userBindService.putPersonBindInvoice(personBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<Optional<PersonBindInvoice>> getPersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                           @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        Optional<PersonBindInvoice> personBindInvoice1 = userBindService.getPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get(), getPeople(deliver).get());
        if (personBindInvoice1.isEmpty()) throw new NoSuchElementFoundException("PersonBindInvoice not found");
        return ResponseEntity.ok().body(personBindInvoice1);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/exist/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<Boolean> existPersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                           @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        boolean bool = userBindService.existPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get(), getPeople(deliver).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/person/bind/invoice/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<Optional<PersonBindInvoice>> deletePersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                              @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        Optional<PersonBindInvoice> personBindInvoice1 = userBindService.getPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get(), getPeople(deliver).get());
        if (personBindInvoice1.isEmpty()) throw new NoSuchElementFoundException("PersonBindInvoice not found");
        return ResponseEntity.ok().body(userBindService.deletePersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get(), getPeople(deliver).get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/deliver/{username}")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoiceForDeliver(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForDeliver(getPeople(username).get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/invoice/{username}")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoiceForInvoice(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForInvoice(getPeople(username).get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoice() {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoice());}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/invoice/{dateFrom}")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoiceDateFrom(@PathVariable("dateFrom") String dateFrom) {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForDateFrom(dateFrom));}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/person/bind/phone")
    ResponseEntity<Optional<PersonBindPhone>> createPersonBindPhone(@RequestBody PersonBindPhone personBindPhone) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/phone").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindPhone(personBindPhone));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/person/bind/phone")
    ResponseEntity<Optional<PersonBindPhone>> putPersonBindPhone(@RequestBody PersonBindPhone personBindPhone) {
        Optional<PersonBindPhone> personBindPhone1 = userBindService.getPersonBindPhone(personBindPhone.getUser());
        if (personBindPhone1.isEmpty()) throw new NoSuchElementFoundException("PersonBindPhone not found");
        return ResponseEntity.ok().body(userBindService.putPersonBindPhone(personBindPhone));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/phone/{username}")
    ResponseEntity<Optional<PersonBindPhone>> getPersonBindPhone(@PathVariable("username") String username) {
        Optional<PersonBindPhone> personBindPhone = userBindService.getPersonBindPhone(getPeople(username).get());
        if (personBindPhone.isEmpty()) throw new NoSuchElementFoundException("PersonBindPhone not found");
        return ResponseEntity.ok().body(personBindPhone);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/phone/exist/{username}")
    ResponseEntity<Boolean> existPersonBindPhone(@PathVariable("username") String username) {
        boolean bool = userBindService.existPersonBindPhone(getPeople(username).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/person/bind/phone/{username}")
    ResponseEntity<Optional<PersonBindPhone>> deletePersonBindPhone(@PathVariable("username") String username) {
        Optional<PersonBindPhone> personBindPhone = userBindService.getPersonBindPhone(getPeople(username).get());
        if (personBindPhone.isEmpty()) throw new NoSuchElementFoundException("PersonBindPhone not found");
        return ResponseEntity.ok().body(userBindService.deletePersonBindPhone(personBindPhone.get().getUser()));}



    private Optional<User> getPeople(String username) {
        Optional<User> user = userService.findUser(username);
        if (user.isEmpty()) throw new NoSuchElementFoundException("Person not found");
        return user;
    }
    private String getDates(Long date) {
        Optional<Dates> dates = datesService.getDate(date);
        if (dates.isEmpty()) throw new NoSuchElementFoundException("Dates not found");
        return dates.get().getDate();
    }
    private Optional<Tour> getTour(String number) {
        if (tourService.existTour(number)) {
            return tourService.getTour(number);
        } else {
            throw new NoSuchElementFoundException("Tour not found");
        }
    }

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/tour/{username}/{tour}")
    ResponseEntity<Optional<PersonBindTour>> getPersonBindTour(@PathVariable("tour") String tour, @PathVariable("username") String username) {
        Optional<PersonBindTour> personBindTour = userBindService.getPersonBindTour(getPeople(username).get(), getTour(tour).get());
        return ResponseEntity.ok().body(personBindTour);}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/person/bind/tour")
    ResponseEntity<Optional<PersonBindTour>> createPersonBindTour(@RequestBody PersonBindTour personBindTour) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/tour").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindTour(personBindTour));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/person/bind/tour")
    ResponseEntity<Optional<PersonBindTour>> updatePersonBindTour(@RequestBody PersonBindTour personBindTour) {
        return ResponseEntity.ok().body(userBindService.savePersonBindTour(personBindTour));}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("/person/bind/tour/{username}/{tour}")
    ResponseEntity<Optional<PersonBindTour>> deletePersonBindTour(@PathVariable("tour") String tour, @PathVariable("username") String username) {
        Optional<PersonBindTour> personBindTour = userBindService.getPersonBindTour(getPeople(username).get(), getTour(tour).get());
        userBindService.deletePersonBindTour(personBindTour);
        return ResponseEntity.ok().body(personBindTour);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/person/bind/tour/{tour}")
    ResponseEntity<Optional<List<PersonBindTour>>> getAllPersonsForTour(@PathVariable("tour") String tour) {
        if (tourService.existTour(tour)) {
            return ResponseEntity.ok().body(userBindService.getAllPersonsForTour(getTour(tour).get()));
        }
        else { throw new NoSuchElementFoundException("Tour not found"); }}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping ("/person/bind/tour/exist/{username}/{tour}")
    ResponseEntity<Boolean> existTourForPerson(@PathVariable("username") String username, @PathVariable("tour") String tour) {
        boolean bool = userBindService.existPersonAndTour(getPeople(username).get(), getTour(tour).get());
        return ResponseEntity.ok().body(bool);}

}
