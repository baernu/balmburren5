package com.messerli.balmburren.controllers;


import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.Work;
import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.WagePayment;
import com.messerli.balmburren.exceptions.NoSuchElementFoundException;
import com.messerli.balmburren.services.DatesService;
import com.messerli.balmburren.services.UserService;
import com.messerli.balmburren.services.WorkService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006","https://www.balmburren.net:4200"}
        , exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/wr/")
@RestController
public class WorkController {
    private WorkService workService;

    private UserService userService;

    private DatesService datesService;

    public WorkController(WorkService workService, UserService userService, DatesService datesService) {
        this.workService = workService;
        this.userService = userService;
        this.datesService = datesService;
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @PostMapping("work/")
    ResponseEntity<Optional<Work>> createWork(@RequestBody Work work) {
        if(!userService.hasUserPermission(work.getUser().getUsername())) throw new AccessDeniedException("Not authorized!");
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/work").toUriString());
        return ResponseEntity.created(uri).body(workService.saveWork(work));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @PutMapping("work/")
    ResponseEntity<Optional<Work>> putWork(@RequestBody Work work) {
        if(!userService.hasUserPermission(work.getUser().getUsername())) throw new AccessDeniedException("Not authorized!");
        Optional<Work> work1 = workService.getWork(work.getUser(), work.getDate());
        if (work1.isEmpty()) throw new NoSuchElementFoundException("Work not found");
        return ResponseEntity.ok().body(workService.putWork(work));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @GetMapping("work/{username}/{date}")
    ResponseEntity<Optional<Work>> getWork(@PathVariable("username") String username, @PathVariable("date") Long date) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        Optional<Work> work1 = workService.getWork(getPeople(username).get(), getDates(date).get());
//        if (work1.isEmpty()) throw new NoSuchElementFoundException("Work not found");
        return ResponseEntity.ok().body(work1);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @DeleteMapping("work/{username}/{date}")
    ResponseEntity<Optional<Work>> deleteWork(@PathVariable("username") String username, @PathVariable("date") Long date) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        Optional<Work> work1 = workService.getWork(getPeople(username).get(), getDates(date).get());
        if (work1.isEmpty()) throw new NoSuchElementFoundException("Work not found");
        return ResponseEntity.ok().body(workService.deleteWork(getPeople(username).get(), getDates(date).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @PatchMapping("work/")
    ResponseEntity<?> deleteWorkById(@RequestBody Work work){
        if(!userService.hasUserPermission(work.getUser().getUsername())) throw new AccessDeniedException("Not authorized!");
        workService.deleteWorkById(work);
        return ResponseEntity.ok().body(work);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @GetMapping("work/{username}")
    ResponseEntity<Optional<List<Work>>> getAllWorksForPeople(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.ok().body(workService.getAllWorksForPeople(getPeople(username).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @GetMapping("work/{username}/{startdate}/{enddate}")
    ResponseEntity<Optional<List<Work>>> getAllWorksForPeopleandIntervall(@PathVariable("username") String username, @PathVariable("startdate") Long startDate, @PathVariable("enddate") Long endDate) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.ok().body(workService.getAllWorksForPeopleAndIntervall(getPeople(username).get(), getDates(startDate).get(), getDates(endDate).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping("wage/payment/")
    ResponseEntity<Optional<WagePayment>> createWagePayment(@RequestBody WagePayment wagePayment) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/wage/payment").toUriString());
        return ResponseEntity.created(uri).body(workService.saveWagePayment(wagePayment));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("wage/payment/")
    ResponseEntity<Optional<WagePayment>> putWagePayment(@RequestBody WagePayment wagePayment) {
        Optional<WagePayment> wagePayment1 = workService.getWagePayment(wagePayment.getUser(), wagePayment.getDateTo());
        if (wagePayment1.isEmpty()) throw new NoSuchElementFoundException("WagePayment not found");
        return ResponseEntity.ok().body(workService.putWagePayment(wagePayment));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @GetMapping("wage/payment/{username}/{date}")
    ResponseEntity<Optional<WagePayment>> getWagePayment(@PathVariable("username") String username, @PathVariable("date") Long date) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        Optional<WagePayment> wagePayment = workService.getWagePayment(getPeople(username).get(), getDates(date).get());
        if (wagePayment.isEmpty()) throw new NoSuchElementFoundException("WagePayment not found");
        return ResponseEntity.ok().body(wagePayment);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("wage/payment/{username}/{date}")
    ResponseEntity<Optional<WagePayment>> deleteWagePayment(@PathVariable("username") String username, @PathVariable("date") Long date) {
        Optional<WagePayment> wagePayment = workService.getWagePayment(getPeople(username).get(), getDates(date).get());
        if (wagePayment.isEmpty()) throw new NoSuchElementFoundException("WagePayment not found");
        return ResponseEntity.ok().body(workService.deleteWagePayment(getPeople(username).get(), getDates(date).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @GetMapping("wage/payment/{username}")
    ResponseEntity<Optional<List<WagePayment>>> getAllWagePaymentsForUser(@PathVariable("username") String username) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.ok().body(workService.getAllWagePaymentsForPeople(getPeople(username).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','DRIVER')")
    @GetMapping("wage/payment/{username}/{startdate}/{enddate}")
    ResponseEntity<Optional<List<WagePayment>>> getAllWagePaymentsForPeopleandIntervall(@PathVariable("username") String username, @PathVariable("startdate") Long startDate, @PathVariable("enddate") Long endDate) {
        if(!userService.hasUserPermission(username)) throw new AccessDeniedException("Not authorized!");
        return ResponseEntity.ok().body(workService.getAllWagePaymentsForPeopleAndIntervall(getPeople(username).get(), getDates(startDate).get(), getDates(endDate).get()));}

    private Optional<User> getPeople(String username) {
        Optional<User> user = userService.findUser(username);
        if(user.isEmpty()) throw new NoSuchElementFoundException("Person not found");
        return user;
    }
    private Optional<Dates> getDates(Long date) {
        Optional<Dates> dates = datesService.getDate(date);
        if (dates.isEmpty()) throw new NoSuchElementFoundException("Dates not found");
        return dates;
    }
}
