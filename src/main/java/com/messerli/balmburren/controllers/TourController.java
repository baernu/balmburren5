package com.messerli.balmburren.controllers;




import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.TourBindDates;
import com.messerli.balmburren.exceptions.NoSuchElementFoundException;
import com.messerli.balmburren.responses.StringResponse;
import com.messerli.balmburren.services.DatesService;
import com.messerli.balmburren.services.ProductService;
import com.messerli.balmburren.services.TourService;
import com.messerli.balmburren.entities.Tour;
import com.messerli.balmburren.entities.TourBindDatesAndProductBindInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006","https://service.balmburren.net:80","https://www.balmburren.net:4200"}
        , exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/tr/")
@RestController
public class TourController {
    private TourService tourService;
    private DatesService datesService;
    private ProductService productService;

    public TourController(TourService tourService, DatesService datesService, ProductService productService) {
        this.tourService = tourService;
        this.datesService = datesService;
        this.productService = productService;
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("tour/")
    ResponseEntity<Optional<Tour>> createTour(@RequestBody Tour tour) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/tour").toUriString());
        return ResponseEntity.created(uri).body(tourService.saveTour(tour));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("tour/")
    ResponseEntity<Optional<Tour>> putTour(@RequestBody Tour tour) {
        Optional<Tour> tour1 = tourService.getTour(tour.getNumber());
        if (tour1.isEmpty()) throw new NoSuchElementFoundException("Tour not found");
        return ResponseEntity.ok().body(tourService.putTour(tour));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/{number}")
    ResponseEntity<Optional<Tour>> getTour(@PathVariable("number") String number) {
        Optional<Tour> tour = tourService.getTour(number);
        if (tour.isEmpty()) throw new NoSuchElementFoundException("Tour not found");
        return ResponseEntity.ok().body(tour);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/")
    ResponseEntity<Optional<List<Tour>>> getTours() {
        return ResponseEntity.ok().body(tourService.getTours());
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @PostMapping("dates/")
    ResponseEntity<Optional<Dates>> createDates(@RequestBody Dates dates) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/dates").toUriString());
        return ResponseEntity.created(uri).body(datesService.saveDate(dates));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("dates/{date}")
    ResponseEntity<Optional<Dates>> getDates(@PathVariable("date") Long id) {
        Optional<Dates> dates = datesService.getDate(id);
        if (dates.isEmpty()) throw new NoSuchElementFoundException("Dates not found");
        return ResponseEntity.ok().body(dates);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("dates/exist/{date}")
    ResponseEntity<Boolean> existDates(@PathVariable("date") String date) {
        boolean bool = datesService.existDate(date);
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("tour/bind/dates/")
    ResponseEntity<Optional<TourBindDates>> createTourBindDates(@RequestBody TourBindDates tourBindDates) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/tour/bind/dates").toUriString());
        return ResponseEntity.created(uri).body(tourService.saveTourBindDates(tourBindDates));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/bind/dates/{number}/{date}")
    ResponseEntity<Optional<TourBindDates>> getTourBindDates(@PathVariable("number") String number, @PathVariable("date") Long date) {
        return ResponseEntity.ok().body(getTourBindDates1(number, date));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping ("tour/bind/dates/{number}/{date}")
    ResponseEntity<?> deleteTourBindDates(@PathVariable("number") String number, @PathVariable("date") Long date) {
        Optional<Tour> tour = tourService.getTour(number);
        if (tour.isEmpty()) throw new NoSuchElementFoundException("Tour not found");
        Optional<Dates> dates = datesService.getDate(date);
        if (dates.isEmpty()) throw new NoSuchElementFoundException("Dates not found");
        tourService.deleteTourBindDates(tour.get(), dates.get());
        return ResponseEntity.ok().body(new StringResponse("Deleted TourBindDates", 200));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/bind/dates/{number}")
    ResponseEntity<Optional<List<TourBindDates>>> getAllTourBindDatesForTour(@PathVariable("number") String number) {
        Optional<Tour> tour = tourService.getTour(number);
        if (tour.isEmpty()) throw new NoSuchElementFoundException("Tour not found");
        return ResponseEntity.ok().body(tourService.getAllTourBindDatesForTour(tour.get()));}


    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("tour/bind/dates/product/infos/")
    ResponseEntity<Optional<TourBindDatesAndProductBindInfo>> createTourBindDatesAndProductBindInfo(@RequestBody TourBindDatesAndProductBindInfo tourBindDatesAndProductBindInfo) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/tour/bind/dates/product/infos").toUriString());
        return ResponseEntity.created(uri).body(tourService.saveTourBindDatesAndProductBindInfos(tourBindDatesAndProductBindInfo));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("tour/bind/dates/product/infos/")
    ResponseEntity<Optional<TourBindDatesAndProductBindInfo>> putTourBindDatesAndProductBindInfo(@RequestBody TourBindDatesAndProductBindInfo tourBindDatesAndProductBindInfo) {
        return ResponseEntity.ok().body(tourService.putTourBindDatesAndProductBindInfos(tourBindDatesAndProductBindInfo));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/bind/dates/product/infos/{tour}/{date}/{product}/{productdetail}")
    ResponseEntity<Optional<TourBindDatesAndProductBindInfo>> getTourBindDatesAndProductInfo(@PathVariable("tour") String number,
                                                                                   @PathVariable("date") Long date,
                                                                                   @PathVariable("product") String name,
                                                                                   @PathVariable("productdetail") Long id) {
        Optional<TourBindDatesAndProductBindInfo> tourBindDatesAndProductBindInfo = tourService.getTourBindDatesAndProductBindInfos(
                tourService.getTour(number).get(), datesService.getDate(date).get(), productService.getProduct(name).get(), productService.getProductDetails(id).get());
        return ResponseEntity.ok().body(tourBindDatesAndProductBindInfo);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/bind/dates/product/infos/{tour}/{date}")
    ResponseEntity<Optional<List<TourBindDatesAndProductBindInfo>>> getAllTourBindDatesAndProductInfoForTourAndDate(@PathVariable("tour") String number,
                                                                                   @PathVariable("date") Long date) {
        Optional<List<TourBindDatesAndProductBindInfo>> list = tourService.getAllTourBindDatesAndProductInfosForTourAndDate(
                tourService.getTour(number).get(), datesService.getDate(date).get());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/bind/dates/product/infos/between/{tour}/{startDate}/{endDate}")
    ResponseEntity<Optional<List<TourBindDatesAndProductBindInfo>>> getAllTourBindDatesAndProductInfoForTourAndDateBetween(@PathVariable("tour") String number,
                                                                                                          @PathVariable("startDate") Long startDate, @PathVariable("endDate") Long endDate) {
        Optional<List<TourBindDatesAndProductBindInfo>> list = tourService.getAllTourBindDatesAndProductInfosForTourAndDateBetween(
                tourService.getTour(number).get(), datesService.getDate(startDate).get(), datesService.getDate(endDate).get());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/bind/dates/product/infos/{tour}")
    ResponseEntity<Optional<List<TourBindDatesAndProductBindInfo>>> getAllTourBindDatesAndProductInfoForTour(@PathVariable("tour") String number) {
        Optional<List<TourBindDatesAndProductBindInfo>> list = tourService.getAllTourBindDatesAndProductInfosForTour(tourService.getTour(number).get());
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/bind/dates/product/infos/")
    ResponseEntity<Optional<List<TourBindDatesAndProductBindInfo>>> getAllTourBindDatesAndProductInfos() {
        Optional<List<TourBindDatesAndProductBindInfo>> list = tourService.getAllTourBindDatesAndProductInfos();
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping ("tour/bind/dates/product/infos/{tour}/{date}/{product}/{productdetail}")
    ResponseEntity<Optional<TourBindDatesAndProductBindInfo>> deleteTourBindDatesAndProductInfo(@PathVariable("tour") String number,
                                                                                   @PathVariable("date") Long date,
                                                                                   @PathVariable("product") String name,
                                                                                   @PathVariable("productdetail") Long id) {
        Optional<TourBindDatesAndProductBindInfo> tourBindDatesAndProductBindInfo = tourService.getTourBindDatesAndProductBindInfos(
                tourService.getTour(number).get(), datesService.getDate(date).get(), productService.getProduct(name).get(), productService.getProductDetails(id).get());
        tourService.deleteTourBindDatesAndProductBindInfos(tourService.getTour(number).get(), datesService.getDate(date).get(),
                productService.getProduct(name).get(), productService.getProductDetails(id).get());
        return ResponseEntity.ok().body(tourBindDatesAndProductBindInfo);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping ("tour/bind/dates/product/infos/")
    ResponseEntity<Optional<TourBindDatesAndProductBindInfo>> deleteTourBindDatesAndProductInfoById(@RequestBody TourBindDatesAndProductBindInfo tourBindDatesAndProductBindInfo) {
        tourService.deleteTourBindDatesAndProductBindInfosById(tourBindDatesAndProductBindInfo);
        return ResponseEntity.ok().body(Optional.ofNullable(tourBindDatesAndProductBindInfo));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("tour/bind/dates/product/infos/exist/{tour}/{date}/{product}/{productdetail}")
    ResponseEntity<Boolean> existTourBindDatesAndProductInfo(@PathVariable("tour") String number,
                                                             @PathVariable("date") Long date,
                                                             @PathVariable("product") String name,
                                                             @PathVariable("productdetail") Long id)  {
        boolean bool = tourService.existTourBindDatesAndProductBindInfo(tourService.getTour(number).get(), datesService.getDate(date).get(),
                productService.getProduct(name).get(), productService.getProductDetails(id).get());
        return ResponseEntity.ok().body(bool);}


    private Optional<TourBindDates> getTourBindDates1(String number, Long date) {
        Optional<Tour> tour = tourService.getTour(number);
        if (tour.isEmpty()) throw new NoSuchElementFoundException("Tour not found");
        Optional<Dates> dates = datesService.getDate(date);
        if (dates.isEmpty()) throw new NoSuchElementFoundException("Dates not found");
        Optional<TourBindDates> tourBindDates = tourService.getTourBindDates(tour.get(), dates.get());
        if (tourBindDates.isEmpty()) throw new NoSuchElementFoundException("TourBindDates not found");
        return tourBindDates;
    }
}
