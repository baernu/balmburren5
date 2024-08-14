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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
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
    @PostMapping("work")
    ResponseEntity<Optional<Work>> createWork(@RequestBody Work work) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/work").toUriString());
        return ResponseEntity.created(uri).body(workService.saveWork(work));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("work")
    ResponseEntity<Optional<Work>> putWork(@RequestBody Work work) {
        Optional<Work> work1 = workService.getWork(work.getUser(), work.getDate());
        if (work1.isEmpty()) throw new NoSuchElementFoundException("Work not found");
        return ResponseEntity.ok().body(workService.putWork(work));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("work/{username}/{date}")
    ResponseEntity<Optional<Work>> getWork(@PathVariable("username") String username, @PathVariable("date") Long date) {
        Optional<Work> work1 = workService.getWork(getPeople(username).get(), getDates(date).get());
        if (work1.isEmpty()) throw new NoSuchElementFoundException("Work not found");
        return ResponseEntity.ok().body(work1);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("work")
    ResponseEntity<Optional<Work>> deleteWork(@PathVariable("username") String username, @PathVariable("date") Long date) {
        Optional<Work> work1 = workService.getWork(getPeople(username).get(), getDates(date).get());
        if (work1.isEmpty()) throw new NoSuchElementFoundException("Work not found");
        return ResponseEntity.ok().body(workService.deleteWork(getPeople(username).get(), getDates(date).get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("work/{username}/{startdate}/{enddate}")
    ResponseEntity<Optional<List<Work>>> getAllWorksForPeopleAndInterval(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(workService.getAllWorksForPeople(getPeople(username).get()));}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("wage/payment")
    ResponseEntity<Optional<WagePayment>> createWagePayment(@RequestBody WagePayment wagePayment) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/wage/payment").toUriString());
        return ResponseEntity.created(uri).body(workService.saveWagePayment(wagePayment));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("wage/payment")
    ResponseEntity<Optional<WagePayment>> putWagePayment(@RequestBody WagePayment wagePayment) {
        Optional<WagePayment> wagePayment1 = workService.getWagePayment(wagePayment.getUser(), wagePayment.getDateTo());
        if (wagePayment1.isEmpty()) throw new NoSuchElementFoundException("WagePayment not found");
        return ResponseEntity.ok().body(workService.putWagePayment(wagePayment));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("wage/payment/{username}/{date}")
    ResponseEntity<Optional<WagePayment>> getWagePayment(@PathVariable("username") String username, @PathVariable("date") Long date) {
        Optional<WagePayment> wagePayment = workService.getWagePayment(getPeople(username).get(), getDates(date).get());
        if (wagePayment.isEmpty()) throw new NoSuchElementFoundException("WagePayment not found");
        return ResponseEntity.ok().body(wagePayment);}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("wage/payment/{username}/{date}")
    ResponseEntity<Optional<WagePayment>> deleteWagePayment(@PathVariable("username") String username, @PathVariable("date") Long date) {
        Optional<WagePayment> wagePayment = workService.getWagePayment(getPeople(username).get(), getDates(date).get());
        if (wagePayment.isEmpty()) throw new NoSuchElementFoundException("WagePayment not found");
        return ResponseEntity.ok().body(workService.deleteWagePayment(getPeople(username).get(), getDates(date).get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("wage/payment/{username}")
    ResponseEntity<Optional<List<WagePayment>>> getAllWagePaymentsForPeople(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(workService.getAllWagePaymentsForPeople(getPeople(username).get()));}



    private Optional<User> getPeople(String username) {
        Optional<User> user = userService.findUser(username);
        throw new NoSuchElementFoundException("Person not found");
    }
    private Optional<Dates> getDates(Long date) {
        Optional<Dates> dates = datesService.getDate(date);
        if (dates.isEmpty()) throw new NoSuchElementFoundException("Dates not found");
        return dates;
    }
}
