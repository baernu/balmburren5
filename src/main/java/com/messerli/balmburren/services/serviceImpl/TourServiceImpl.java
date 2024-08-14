package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.repositories.TourBindDatesAndProductBindInfoRepo;
import com.messerli.balmburren.repositories.TourBindDatesRepo;
import com.messerli.balmburren.repositories.TourRepo;
import com.messerli.balmburren.services.ProductService;
import com.messerli.balmburren.services.TourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TourServiceImpl implements TourService {
    private final TourRepo tourRepo;
    private final TourBindDatesRepo tourBindDatesRepo;
    private final TourBindDatesAndProductBindInfoRepo tourBindDatesAndProductBindInfoRepo;
    private ProductService productService;

    public TourServiceImpl(TourRepo tourRepo, TourBindDatesRepo tourBindDatesRepo, TourBindDatesAndProductBindInfoRepo tourBindDatesAndProductBindInfoRepo, ProductService productService) {
        this.tourRepo = tourRepo;
        this.tourBindDatesRepo = tourBindDatesRepo;
        this.tourBindDatesAndProductBindInfoRepo = tourBindDatesAndProductBindInfoRepo;
        this.productService = productService;
    }

    @Override
    public Optional<Tour> saveTour(Tour tour) {
        log.info("Saving new Tour: {} to the database", tour.getNumber());
        return Optional.of(tourRepo.save(tour));
    }

    @Override
    public Optional<Tour> putTour(Tour tour) {
        log.info("Make changes on Tour: {}.", tour.getNumber());
        return Optional.of(tourRepo.save(tour));
    }

    @Override
    public Optional<Tour> getTour(String number) {
        Optional<Tour> tour = tourRepo.findTourByNumber(number);
        log.info("Tour is: {}", tour.get().getNumber());
        return tourRepo.findTourByNumber(number);
    }

    @Override
    public boolean existTour(String number) {
        boolean isExist = tourRepo.existsByNumber(number);
        log.info("Tour exist: {}", isExist);
        return isExist;
    }

    @Override
    public Optional<List<Tour>> getTours() {
        List<Tour> tours = tourRepo.findAll();
        log.info("get all Tours.");
        return Optional.of(tourRepo.findAll());
    }

    @Override
    public Optional<TourBindDates> saveTourBindDates(TourBindDates tourBindDates) {
        log.info("Saving new TourBindDates: {}", tourBindDates);
        return Optional.of(tourBindDatesRepo.save(tourBindDates));
    }

    @Override
    public Optional<TourBindDates> getTourBindDates(Tour tour, Dates dates) {
        Optional<TourBindDates> tourBindDates = tourBindDatesRepo.findByTourAndDates_Date(tour, dates.getDate());
        log.info("TourBindDates is: {}", tourBindDates.get());
        return tourBindDates;
    }

    @Override
    public void deleteTourBindDates(Tour tour, Dates dates) {
        Optional<TourBindDates> tourBindDates = tourBindDatesRepo.findByTourAndDates_Date(tour, dates.getDate());
        log.info("Delete TourBindDates: {}", tourBindDates.get());
        tourBindDatesRepo.delete(tourBindDates.get());
//        return tourBindDates;
    }

    @Override
    public Optional<List<TourBindDates>> getAllTourBindDatesForTour(Tour tour) {
        Optional<List<TourBindDates>> list = tourBindDatesRepo.findByTour(tour, sortByDate());
        log.info("get all TourBindDates for tour: {}, List: {}", tour, list.get());
        return list;
    }

    @Override
    public Optional<TourBindDatesAndProductBindInfo> saveTourBindDatesAndProductBindInfos(TourBindDatesAndProductBindInfo tourBindDatesAndProductBindInfo) {
        log.info("Saving TourBindDatesAndProductBindInfo: {}", tourBindDatesAndProductBindInfo);
        return Optional.of(tourBindDatesAndProductBindInfoRepo.save(tourBindDatesAndProductBindInfo));
    }

    @Override
    public Optional<TourBindDatesAndProductBindInfo> putTourBindDatesAndProductBindInfos(TourBindDatesAndProductBindInfo tourBindDatesAndProductBindInfo) {
        log.info("Updating TourBindDatesAndProductBindInfo: {}", tourBindDatesAndProductBindInfo);
        return Optional.of(tourBindDatesAndProductBindInfoRepo.save(tourBindDatesAndProductBindInfo));
    }

    public Optional<TourBindDatesAndProductBindInfo> getTourBindDatesAndProductBindInfos(Tour tour, Dates date, Product product, ProductDetails productDetails) {
        Optional<ProductBindProductDetails> productBindInfos = productService.getProductBindInfos(product, productDetails);
        Optional<TourBindDatesAndProductBindInfo> tourBindDatesAndProductBindInfo = tourBindDatesAndProductBindInfoRepo.findByTourAndDates_DateAndProductBindInfos(tour, date.getDate(), productBindInfos);
        log.info("Getting TourBindDatesAndProductBindInfo: {}", tourBindDatesAndProductBindInfo.get());
        return tourBindDatesAndProductBindInfo;
    }

    @Override
    public void deleteTourBindDatesAndProductBindInfos(Tour tour, Dates date, Product product, ProductDetails productDetails) {
        Optional<ProductBindProductDetails> productBindInfos = productService.getProductBindInfos(product, productDetails);
        Optional<TourBindDatesAndProductBindInfo> tourBindDatesAndProductBindInfo = tourBindDatesAndProductBindInfoRepo.findByTourAndDates_DateAndProductBindInfos(tour, date.getDate(), productBindInfos);
        log.info("Deleting TourBindDatesAndProductBindInfo: {}", tourBindDatesAndProductBindInfo);
        tourBindDatesAndProductBindInfoRepo.delete(tourBindDatesAndProductBindInfo.get());;
    }

    @Override
    public boolean existTourBindDatesAndProductBindInfo(Tour tour, Dates date, Product product, ProductDetails productDetails) {
        Optional<ProductBindProductDetails> productBindInfos = productService.getProductBindInfos(product, productDetails);
        boolean isExist = tourBindDatesAndProductBindInfoRepo.existsByTourAndDates_DateAndProductBindInfos(tour, date.getDate(), productBindInfos);
        log.info("TourBindDatesAndProductBindInfo exist: {}", isExist);
        return isExist;
    }

    @Override
    public Optional<List<TourBindDatesAndProductBindInfo>> getAllTourBindDatesAndProductInfosForTourAndDate(Tour tour, Dates date) {
        Optional<List<TourBindDatesAndProductBindInfo>> list = tourBindDatesAndProductBindInfoRepo.findAllByTourAndDates_Date(tour, date.getDate());
        log.info("Getting All TourBindDatesAndProductBindInfos for tour and dates: {}", list.get());
        return list;
    }

    @Override
    public Optional<List<TourBindDatesAndProductBindInfo>> getAllTourBindDatesAndProductInfosForTourAndDateBetween(Tour tour, Dates startDate, Dates endDate) {
        Optional<List<TourBindDatesAndProductBindInfo>> list = tourBindDatesAndProductBindInfoRepo.findAllByTourAndDates_DateBetween(tour, startDate.getDate(), endDate.getDate());
        log.info("Getting All TourBindDatesAndProductBindInfos: {} for tour and dates between startDate: {}, endDate: {}", list.get(), startDate, endDate);
        return list;
    }

    @Override
    public Optional<List<TourBindDatesAndProductBindInfo>> getAllTourBindDatesAndProductInfosForTour(Tour tour) {
        Optional<List<TourBindDatesAndProductBindInfo>> list = tourBindDatesAndProductBindInfoRepo.findAllByTour(tour);
        log.info("Getting All TourBindDatesAndProductBindInfos for tour: {}", list.get());
        return list;
    }

    @Override
    public Optional<List<TourBindDatesAndProductBindInfo>> getAllTourBindDatesAndProductInfos() {
        List<TourBindDatesAndProductBindInfo> list = tourBindDatesAndProductBindInfoRepo.findAll();
        log.info("Getting All TourBindDatesAndProductBindInfos: {}", list);
        return Optional.of(list);
    }

    private Sort sortByDate() {
        Sort sortBy = Sort.by(Sort.Direction.ASC, "dates.date");
        return sortBy;
    }
}
