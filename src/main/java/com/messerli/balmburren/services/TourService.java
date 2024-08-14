package com.messerli.balmburren.services;
import com.messerli.balmburren.entities.*;

import java.util.List;
import java.util.Optional;

public interface TourService {

    Optional<Tour> saveTour(Tour tour);
    Optional<Tour> putTour(Tour tour);
    Optional<Tour> getTour(String number);
    boolean existTour(String number);
    Optional<List<Tour>> getTours();
    Optional<TourBindDates> saveTourBindDates(TourBindDates tourBindDates);
    Optional<TourBindDates> getTourBindDates(Tour tour, Dates dates);
    void deleteTourBindDates(Tour tour, Dates dates);
    Optional<List<TourBindDates>> getAllTourBindDatesForTour(Tour tour);
    Optional<TourBindDatesAndProductBindInfo> saveTourBindDatesAndProductBindInfos(TourBindDatesAndProductBindInfo tourBindDatesAndProductBindInfo);
    Optional<TourBindDatesAndProductBindInfo> putTourBindDatesAndProductBindInfos(TourBindDatesAndProductBindInfo tourBindDatesAndProductBindInfo);
    Optional<TourBindDatesAndProductBindInfo> getTourBindDatesAndProductBindInfos(Tour tour, Dates date, Product product, ProductDetails productDetails);
    void deleteTourBindDatesAndProductBindInfos(Tour tour, Dates date, Product product, ProductDetails productDetails);
    boolean existTourBindDatesAndProductBindInfo(Tour tour, Dates date, Product product, ProductDetails productDetails);
    Optional<List<TourBindDatesAndProductBindInfo>> getAllTourBindDatesAndProductInfosForTourAndDate(Tour tour, Dates date);
    Optional<List<TourBindDatesAndProductBindInfo>> getAllTourBindDatesAndProductInfosForTourAndDateBetween(Tour tour, Dates startDate, Dates endDate);
    Optional<List<TourBindDatesAndProductBindInfo>> getAllTourBindDatesAndProductInfosForTour(Tour tour);
    Optional<List<TourBindDatesAndProductBindInfo>> getAllTourBindDatesAndProductInfos();
}
