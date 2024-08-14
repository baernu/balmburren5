package com.messerli.balmburren.controllers;


import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.exceptions.NoSuchElementFoundException;
import com.messerli.balmburren.services.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/or/")
@RestController
public class OrderController {

    private OrderService orderService;

    private ProductService productService;

    private DatesService datesService;

    private UserService userService;

    private TourService tourService;

    public OrderController(OrderService orderService, ProductService productService, DatesService datesService, UserService userService, TourService tourService) {
        this.orderService = orderService;
        this.productService = productService;
        this.datesService = datesService;
        this.userService = userService;
        this.tourService = tourService;
    }

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("order")
    ResponseEntity<Optional<Ordered>> createOrder(@RequestBody Ordered ordered) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/order").toUriString());
        return ResponseEntity.created(uri).body(orderService.saveOrder(ordered));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("order")
    ResponseEntity<Optional<Ordered>> putOrder(@RequestBody Ordered ordered) {
        return ResponseEntity.ok().body(orderService.putOrder(ordered));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/{people}/{product}/{productdetails}/{date}/{tour}")
    ResponseEntity<Optional<Ordered>> getOrder(@PathVariable("people") String username,
                                     @PathVariable("product") String product, @PathVariable("productdetails") Long id,
                                     @PathVariable("date") Long date, @PathVariable("tour") String tour) {
        return ResponseEntity.ok().body(getOrdered(username, product, id, date, tour, 1));}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("order/{people}/{product}/{productdetails}/{date}/{tour}")
    ResponseEntity<Optional<Ordered>> deleteOrder(@PathVariable("people") String username,
                                     @PathVariable("product") String product, @PathVariable("productdetails") Long id,
                                        @PathVariable("date") Long date, @PathVariable("tour") String tour) {
        return ResponseEntity.ok().body(getOrdered(username, product, id, date, tour,0));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/exist/{people}/{product}/{productdetails}/{date}/{tour}")
    ResponseEntity<Boolean> existOrder(@PathVariable("people") String username,
                                     @PathVariable("product") String product, @PathVariable("productdetails") Long id,
                                       @PathVariable("date") Long date, @PathVariable("tour") String tour) {
        Optional<User> user = userService.findUser(username);
        if (user.isEmpty()) throw new NoSuchElementFoundException("Person not found");
        Optional<ProductBindProductDetails> productBindProductDetails = getProductBindInfo(product, id);
        Optional<Dates> dates = datesService.getDate(date);
        if (dates.isEmpty()) throw new NoSuchElementFoundException("Date not found");
        return ResponseEntity.ok().body(orderService.existOrderForDatePeopleAndProductBindInfosAndTour(dates.get().getDate(),user.get(),productBindProductDetails.get(), getTour(tour).get()));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/{people}")
    ResponseEntity<Optional<List<Ordered>>> getAllOrderForPerson(@PathVariable("people") String username) {
        Optional<User> user = userService.findUser(username);
        if (user.isEmpty()) throw new NoSuchElementFoundException("Person not found");
        Optional<List<Ordered>> list = orderService.getAllOrderForPeople(user.get());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/between/{startDate}/{endDate}/{people}")
    ResponseEntity<Optional<List<Ordered>>> getAllOrderForPersonBetween(@PathVariable("startDate") Long startDate, @PathVariable("endDate") Long endDate,
                                                              @PathVariable("people") String username) {
        Optional<User> user = userService.findUser(username);
        if (user.isEmpty()) throw new NoSuchElementFoundException("Person not found");
        Optional<List<Ordered>> list = orderService.getAllOrderForPeopleBetween(getDates(startDate).get().getDate(), getDates(endDate).get().getDate(), user.get());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/{tour}/{date}")
    ResponseEntity<Optional<List<Ordered>>> getAllOrderForTourAndDate(@PathVariable("tour") String tour, @PathVariable("date") Long date) {
        Optional<List<Ordered>> list = orderService.getAllOrderForTourAndDate(getTour(tour).get(), getDates(date).get().getDate());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PostMapping("order/person/profile")
    ResponseEntity<Optional<PersonProfileOrder>> createPersonProfileOrder(@RequestBody PersonProfileOrder personProfileOrder) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/order/person/profile").toUriString());
        return ResponseEntity.created(uri).body(orderService.savePersonProfileOrder(personProfileOrder));}

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("order/person/profile")
    ResponseEntity<Optional<PersonProfileOrder>> putPersonProfileOrder(@RequestBody PersonProfileOrder personProfileOrder) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/order/person/profile").toUriString());
        return ResponseEntity.created(uri).body(orderService.putPersonProfileOrder(personProfileOrder));}

    @CrossOrigin( allowCredentials = "true")
    @DeleteMapping("order/person/profile/{people}/{product}/{productdetails}/{tour}")
    ResponseEntity<Optional<PersonProfileOrder>> deletePersonProfileOrder(@PathVariable("people") String username,
                                     @PathVariable("product") String product, @PathVariable("productdetails") Long id, @PathVariable("tour") String tour) {
        Optional<PersonProfileOrder> personProfileOrder = orderService.getPersonProfileOrder(getPerson(username).get(), getProductBindInfo(product, id).get(), getTour(tour).get());
        orderService.deletePersonProfileOrder(getPerson(username).get(), getProductBindInfo(product, id).get(), getTour(tour).get());
        return ResponseEntity.ok().body(personProfileOrder);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/person/profile/{people}/{product}/{productdetails}/{tour}")
    ResponseEntity<Optional<PersonProfileOrder>> getPersonProfileOrder(@PathVariable("people") String username,
                                                             @PathVariable("product") String product, @PathVariable("productdetails") Long id, @PathVariable("tour") String tour) {
        Optional<PersonProfileOrder> personProfileOrder = orderService.getPersonProfileOrder(getPerson(username).get(), getProductBindInfo(product, id).get(), getTour(tour).get());
        return ResponseEntity.ok().body(personProfileOrder);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/person/profile/exist/{people}/{product}/{productdetails}/{tour}")
    ResponseEntity<Boolean> existPersonProfileOrder(@PathVariable("people") String username,
                                                             @PathVariable("product") String product, @PathVariable("productdetails") Long id, @PathVariable("tour") String tour) {
        Boolean bool = orderService.existPersonProfileOrder(getPerson(username).get(), getProductBindInfo(product, id).get(), getTour(tour).get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/person/profile/{people}")
    ResponseEntity<Optional<List<PersonProfileOrder>>> getAllPersonProfileOrderForPerson(@PathVariable("people") String username) {
        Optional<List<PersonProfileOrder>> list = orderService.getAllPersonProfileOrderForPerson(getPerson(username).get());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("order/person/profile")
    ResponseEntity<Optional<List<PersonProfileOrder>>> getAllPersonProfileOrder() {
        Optional<List<PersonProfileOrder>> list = orderService.getAllPersonProfileOrder();
        return ResponseEntity.ok().body(list);}

    private Optional<Ordered> getOrdered(String username, String product, Long id, Long date, String tour, int i) {
        Optional<Ordered> ordered = null;
        Optional<User> user = getPerson(username);
        Optional<Dates> dates = getDates(date);
        if (i == 1) ordered = orderService.getOrder(user.get(), getProductBindInfo(product, id).get(), dates.get(), getTour(tour).get());
        if (i == 0) ordered = orderService.deleteOrder(user.get(), getProductBindInfo(product, id).get(), dates.get(), getTour(tour).get());
        if (ordered.isEmpty()) throw new NoSuchElementFoundException("Order not found");
        return ordered;
    }
    private Optional<User> getPerson(String username) {
        Optional<User> user = userService.findUser(username);
        if (user.isEmpty()) throw new NoSuchElementFoundException("Person not found");
        return user;
    }
    private Optional<Dates> getDates(Long id){
        Optional<Dates> dates = datesService.getDate(id);
        if (dates.isEmpty()) throw new NoSuchElementFoundException("Date not found");
        return dates;
    }
    private Optional<Tour> getTour(String number) {
        Optional<Tour> tour = tourService.getTour(number);
        if (tour.isEmpty()) throw new NoSuchElementFoundException("Tour not found");
        return tour;
    }
    private Optional<ProductBindProductDetails> getProductBindInfo(String product, Long id) {
        Optional<Product> product1 = productService.getProduct(product);
        if (product1.isEmpty()) throw new NoSuchElementFoundException("Product not found");
        Optional<ProductDetails> productDetails = productService.getProductDetails(id);
        if(productDetails.isEmpty()) throw new NoSuchElementFoundException("ProductDetail not found");
        Optional<ProductBindProductDetails> productBindInfos = productService.getProductBindInfos(product1.get(), productDetails.get());
        if (productBindInfos.isEmpty()) throw new NoSuchElementFoundException("ProductBindInfo not found");
        return productBindInfos;
    }


}
