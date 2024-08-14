package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.ProductBindProductDetails;
import com.messerli.balmburren.entities.Tour;
import com.messerli.balmburren.entities.TourBindDatesAndProductBindInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TourBindDatesAndProductBindInfoRepo extends JpaRepository<TourBindDatesAndProductBindInfo, Long> {
    Optional<TourBindDatesAndProductBindInfo> findByTourAndDates_DateAndProductBindInfos(Tour tour, String date, Optional<ProductBindProductDetails> productBindInfos);
    boolean existsByTourAndDates_DateAndProductBindInfos(Tour tour, String date, Optional<ProductBindProductDetails> productBindInfos);
    Optional<List<TourBindDatesAndProductBindInfo>> findAllByTourAndDates_Date(Tour tour, String date);
    Optional<List<TourBindDatesAndProductBindInfo>> findAllByTourAndDates_DateBetween(Tour tour, String startDate, String endDate);
    Optional<List<TourBindDatesAndProductBindInfo>> findAllByTour(Tour tour);

    void delete(Optional<TourBindDatesAndProductBindInfo> tourBindDatesAndProductBindInfo);
}
