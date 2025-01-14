package com.messerli.balmburren.controllers;


import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.exceptions.NoSuchElementFoundException;
import com.messerli.balmburren.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006","https://www.balmburren.net:4200"}
        , exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping("address/")
    ResponseEntity<Optional<Address>> createAddress(@RequestBody Address address) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/address").toUriString());
        return ResponseEntity.created(uri).body(userBindService.saveAddress(address));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @PutMapping("address/")
    ResponseEntity<Optional<Address>> putAddress(@RequestBody Address address) {
        Optional<Address> address1 = userBindService.getAddress(address.getId());
        if (!address1.isPresent()) throw new NoSuchElementFoundException("Address not found");
        return ResponseEntity.ok().body(userBindService.putAddress(address));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("address/{id}")
    ResponseEntity<Optional<Address>> deleteAddress(@PathVariable("id") Long id) {
        Optional<Address> address1 = userBindService.getAddress(id);
        if (!address1.isPresent()) throw new NoSuchElementFoundException("Address not found");
        return ResponseEntity.ok().body(userBindService.deleteAddress(address1.get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("address/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    ResponseEntity<Optional<Address>> getAddress(@PathVariable("id") Long id) {
        Optional<Address> address = userBindService.getAddress(id);
        if (!address.isPresent()) throw new NoSuchElementFoundException("Address not found");
//        else {
        return ResponseEntity.ok().body(address);}



    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("person/bind/deliveraddress/")
    ResponseEntity<Optional<PersonBindDeliverAddress>> createPersonBindDeliverAddress(@RequestBody PersonBindDeliverAddress personBindDeliverAddress) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/deliveraddress").toUriString());
        if(!userService.hasUserPermission(personBindDeliverAddress.getUser().getUsername())) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.created(uri).body(userBindService.savePersonBindDeliverAddress(personBindDeliverAddress));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @PutMapping("person/bind/deliveraddress/")
    ResponseEntity<Optional<PersonBindDeliverAddress>> putPersonBindDeliverAddress(@RequestBody PersonBindDeliverAddress personBindDeliverAddress) {
        if(!userService.hasUserPermission(personBindDeliverAddress.getUser().getUsername())) throw new AccessDeniedException("Not authorized!");
        Optional<PersonBindDeliverAddress> personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(personBindDeliverAddress.getUser());
        if (personBindDeliverAddress1.isEmpty()) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(userBindService.putPersonBindDeliverAddress(personBindDeliverAddress));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("person/bind/deliveraddress/{username}")
    ResponseEntity<Optional<PersonBindDeliverAddress>> getPersonBindDeliverAddress(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        Optional<PersonBindDeliverAddress> personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(getPeople(username).get());
        if (personBindDeliverAddress1.isEmpty()) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(personBindDeliverAddress1);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("person/bind/deliveraddress/exist/{username}")
    ResponseEntity<Boolean> existPersonBindDeliverAddress(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        boolean bool = userBindService.existPersonBindDeliverAddress(getPeople(username).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("person/bind/deliveraddress/{people}")
    ResponseEntity<Optional<PersonBindDeliverAddress>> deletePersonBindDeliverAddress(@PathVariable("username") String username) {
        Optional<PersonBindDeliverAddress> personBindDeliverAddress1 = userBindService.getPersonBindDeliverAddress(getPeople(username).get());
        if (personBindDeliverAddress1.isEmpty()) throw new NoSuchElementFoundException("PersonBindDeliverAddress not found");
        return ResponseEntity.ok().body(userBindService.deletePersonBindDeliverAddress(personBindDeliverAddress1.get().getUser()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("person/bind/invoice/")
    ResponseEntity<Optional<PersonBindInvoice>> createPersonBindInvoice(@RequestBody PersonBindInvoice personBindInvoice) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/invoice").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindInvoice(personBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("person/bind/invoice/")
    ResponseEntity<Optional<PersonBindInvoice>> putPersonBindInvoice(@RequestBody PersonBindInvoice personBindInvoice) {
        return ResponseEntity.ok().body(userBindService.putPersonBindInvoice(personBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("person/bind/invoice/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<Optional<PersonBindInvoice>> getPersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                           @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        Optional<PersonBindInvoice> personBindInvoice1 = userBindService.getPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get(), getPeople(deliver).get());
        if(!userService.hasUserPermission(personBindInvoice1.get().getPersonInvoice().getUsername())) throw new AccessDeniedException("Not authorized!");
        if (personBindInvoice1.isEmpty()) throw new NoSuchElementFoundException("PersonBindInvoice not found");
        return ResponseEntity.ok().body(personBindInvoice1);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("person/bind/invoice/exist/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<Boolean> existPersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                           @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        boolean bool = userBindService.existPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get(), getPeople(deliver).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("person/bind/invoice/{dateFrom}/{dateTo}/{invoice}/{deliver}")
    ResponseEntity<Optional<PersonBindInvoice>> deletePersonBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                              @PathVariable("invoice") String invoice, @PathVariable("deliver") String deliver) {
        Optional<PersonBindInvoice> personBindInvoice1 = userBindService.getPersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get(), getPeople(deliver).get());
        if (personBindInvoice1.isEmpty()) throw new NoSuchElementFoundException("PersonBindInvoice not found");
        return ResponseEntity.ok().body(userBindService.deletePersonBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get(), getPeople(deliver).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping("person/bind/invoice/")
    ResponseEntity<Optional<PersonBindInvoice>> deletePersonBindInvoiceById(@RequestBody PersonBindInvoice personBindInvoice) {
    userBindService.deletePersonBindInvoiceById(personBindInvoice);
        return ResponseEntity.ok().body(Optional.ofNullable(personBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("person/bind/invoice/invoice/{username}")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoiceForInvoice(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForInvoice(getPeople(username).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("person/bind/invoice/")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoice() {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoice());}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("person/bind/invoice/{dateFrom}")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoiceDateFrom(@PathVariable("dateFrom") String dateFrom) {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForDateFrom(dateFrom));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("person/bind/invoice/dateto/{dateTo}")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoiceDateTo(@PathVariable("dateTo") String dateTo) {
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForDateTo(dateTo));}




    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER')")
    @PostMapping("driver/bind/invoice/")
    ResponseEntity<Optional<DriverBindInvoice>> createDriverBindInvoice(@RequestBody DriverBindInvoice driverBindInvoice) {
        if(!userService.hasUserPermission(driverBindInvoice.getPersonInvoice().getUsername())) throw new AccessDeniedException("Not authorized!");
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/driver/bind/invoice").toUriString());
        return ResponseEntity.created(uri).body(userBindService.saveDriverBindInvoice(driverBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER')")
    @PutMapping("driver/bind/invoice/")
    ResponseEntity<Optional<DriverBindInvoice>> putDriverBindInvoice(@RequestBody DriverBindInvoice driverBindInvoice) {
        if(!userService.hasUserPermission(driverBindInvoice.getPersonInvoice().getUsername())) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.ok().body(userBindService.putDriverBindInvoice(driverBindInvoice));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER')")
    @GetMapping("driver/bind/invoice/{dateFrom}/{dateTo}/{invoice}")
    ResponseEntity<Optional<DriverBindInvoice>> getDriverBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                                     @PathVariable("invoice") String invoice) {
        Optional<DriverBindInvoice> driverBindInvoice1 = userBindService.getDriverBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get());
        if(!userService.hasUserPermission(driverBindInvoice1.get().getPersonInvoice().getUsername())) throw new AccessDeniedException("Not authorized!");
        if (driverBindInvoice1.isEmpty()) throw new NoSuchElementFoundException("DriverBindInvoice not found");
        return ResponseEntity.ok().body(driverBindInvoice1);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER')")
    @GetMapping("driver/bind/invoice/exist/{dateFrom}/{dateTo}/{invoice}")
    ResponseEntity<Boolean> existDriverBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                   @PathVariable("invoice") String invoice) {
        boolean bool = userBindService.existDriverBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("driver/bind/invoice/{dateFrom}/{dateTo}/{invoice}")
    ResponseEntity<Optional<DriverBindInvoice>> deleteDriverBindInvoice(@PathVariable("dateFrom") Long dateFrom, @PathVariable("dateTo") Long dateTo,
                                                                        @PathVariable("invoice") String invoice) {
        Optional<DriverBindInvoice> driverBindInvoice1 = userBindService.getDriverBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get());
        if (driverBindInvoice1.isEmpty()) throw new NoSuchElementFoundException("DriverBindInvoice not found");
        return ResponseEntity.ok().body(userBindService.deleteDriverBindInvoice(getDates(dateFrom), getDates(dateTo), getPeople(invoice).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping("driver/bind/invoice/")
    ResponseEntity<Optional<DriverBindInvoice>> deleteDriverBindInvoiceById(@RequestBody DriverBindInvoice driverBindInvoice) {
        userBindService.deleteDriverBindInvoiceById(driverBindInvoice);
        return ResponseEntity.ok().body(Optional.ofNullable(driverBindInvoice));}


    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'USER','KATHY')")
    @GetMapping("person/bind/invoice/deliver/{username}")
    ResponseEntity<Optional<List<PersonBindInvoice>>> getAllPersonBindInvoiceForDeliver(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.ok().body(userBindService.getAllPersonBindInvoiceForDeliver(getPeople(username).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER')")
    @GetMapping("driver/bind/invoice/invoice/{username}")
    ResponseEntity<Optional<List<DriverBindInvoice>>> getAllDriverBindInvoiceForInvoice(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.ok().body(userBindService.getAllDriverBindInvoiceForInvoice(getPeople(username).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("driver/bind/invoice/")
    ResponseEntity<Optional<List<DriverBindInvoice>>> getAllDriverBindInvoice() {
        return ResponseEntity.ok().body(userBindService.getAllDriverBindInvoice());}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("driver/bind/invoice/{dateFrom}")
    ResponseEntity<Optional<List<DriverBindInvoice>>> getAllDriverBindInvoiceDateFrom(@PathVariable("dateFrom") String dateFrom) {
        return ResponseEntity.ok().body(userBindService.getAllDriverBindInvoiceForDateFrom(dateFrom));}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("person/bind/phone/")
    ResponseEntity<Optional<PersonBindPhone>> createPersonBindPhone(@RequestBody PersonBindPhone personBindPhone) {
        if(!userService.hasUserPermission(personBindPhone.getUser().getUsername())) throw new AccessDeniedException("Not authorized!");
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/phone").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindPhone(personBindPhone));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @PutMapping("person/bind/phone/")
    ResponseEntity<Optional<PersonBindPhone>> putPersonBindPhone(@RequestBody PersonBindPhone personBindPhone) {
        if(!userService.hasUserPermission(personBindPhone.getUser().getUsername())) throw new AccessDeniedException("Not authorized!");
        Optional<PersonBindPhone> personBindPhone1 = userBindService.getPersonBindPhone(personBindPhone.getUser());
        if (personBindPhone1.isEmpty()) throw new NoSuchElementFoundException("PersonBindPhone not found");
        return ResponseEntity.ok().body(userBindService.putPersonBindPhone(personBindPhone));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("person/bind/phone/{username}")
    ResponseEntity<Optional<PersonBindPhone>> getPersonBindPhone(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        Optional<PersonBindPhone> personBindPhone = userBindService.getPersonBindPhone(getPeople(username).get());
        if (personBindPhone.isEmpty()) throw new NoSuchElementFoundException("PersonBindPhone not found");
        return ResponseEntity.ok().body(personBindPhone);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("person/bind/phone/exist/{username}")
    ResponseEntity<Boolean> existPersonBindPhone(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        boolean bool = userBindService.existPersonBindPhone(getPeople(username).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("person/bind/phone/{username}")
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("person/bind/tour/{username}/{tour}")
    ResponseEntity<Optional<PersonBindTour>> getPersonBindTour(@PathVariable("tour") String tour, @PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        Optional<PersonBindTour> personBindTour = userBindService.getPersonBindTour(getPeople(username).get(), getTour(tour).get());
        return ResponseEntity.ok().body(personBindTour);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("person/bind/tour/")
    ResponseEntity<Optional<PersonBindTour>> createPersonBindTour(@RequestBody PersonBindTour personBindTour) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/person/bind/tour").toUriString());
        return ResponseEntity.created(uri).body(userBindService.savePersonBindTour(personBindTour));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("person/bind/tour/")
    ResponseEntity<Optional<PersonBindTour>> updatePersonBindTour(@RequestBody PersonBindTour personBindTour) {
        return ResponseEntity.ok().body(userBindService.savePersonBindTour(personBindTour));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("person/bind/tour/{username}/{tour}")
    ResponseEntity<Optional<PersonBindTour>> deletePersonBindTour(@PathVariable("tour") String tour, @PathVariable("username") String username) {
        Optional<PersonBindTour> personBindTour = userBindService.getPersonBindTour(getPeople(username).get(), getTour(tour).get());
        userBindService.deletePersonBindTour(personBindTour);
        return ResponseEntity.ok().body(personBindTour);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER')")
    @GetMapping("person/bind/tour/{tour}")
    ResponseEntity<Optional<List<PersonBindTour>>> getAllPersonsForTour(@PathVariable("tour") String tour) {
        if (tourService.existTour(tour)) {
            return ResponseEntity.ok().body(userBindService.getAllPersonsForTour(getTour(tour).get()));
        }
        else { throw new NoSuchElementFoundException("Tour not found"); }}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("person/bind/tour/exist/{username}/{tour}")
    ResponseEntity<Boolean> existTourForPerson(@PathVariable("username") String username, @PathVariable("tour") String tour) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        boolean bool = userBindService.existPersonAndTour(getPeople(username).get(), getTour(tour).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("isAuthenticated()")
    @GetMapping ("person/bind/role/me")
    ResponseEntity<List<UsersRole>>getAllPersonBindRolesMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails currentUser = (MyUserDetails) authentication.getPrincipal();
        List<UsersRole> list = userBindService.getAllUserBindRoles(getPeople(currentUser.getUsername()).get());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'KATHY')")
    @GetMapping ("person/bind/role/{username}")
    ResponseEntity<List<UsersRole>>getAllPersonBindRoles(@PathVariable("username") String username) {
        List<UsersRole> list = userBindService.getAllUserBindRoles(getPeople(username).get());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping ("person/bind/role/")
    ResponseEntity<Optional<UsersRole>> savePersonBindRole(@RequestBody UsersRole usersRole) {
        Optional<UsersRole> usersRole1 = userBindService.savePersonBindRole(usersRole);
        return ResponseEntity.ok().body(usersRole1);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping("person/bind/role/")
    ResponseEntity<Optional<UsersRole>> deletePersonBindRole(@RequestBody UsersRole usersRole) {
         userBindService.deletePersonBindRole(usersRole);
        return ResponseEntity.ok().body(Optional.ofNullable(usersRole));}

}
