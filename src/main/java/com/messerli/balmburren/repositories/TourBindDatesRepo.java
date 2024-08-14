package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.Tour;
import com.messerli.balmburren.entities.TourBindDates;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TourBindDatesRepo extends JpaRepository<TourBindDates, Long> {

    Optional<TourBindDates> findByTourAndDates_Date(Tour tour, String date);
    Optional<List<TourBindDates>> findByTour(Tour tour, Sort orders);


    void delete(Optional<TourBindDates> tourBindDates);
}
