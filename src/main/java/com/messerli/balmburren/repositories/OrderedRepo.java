package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.Ordered;
import com.messerli.balmburren.entities.ProductBindProductDetails;
import com.messerli.balmburren.entities.Tour;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderedRepo extends JpaRepository<Ordered, Long> {
    Optional<Ordered> findByDeliverPeopleAndProductBindInfosAndDate_DateAndTour(User user, ProductBindProductDetails productAndInfo, String date, Tour tour);
    Optional<List<Ordered>> findAllByDeliverPeople(User user);
    Optional<List<Ordered>> findAllByDate_DateBetweenAndDeliverPeople(String dateDateStart, String dateDateEnd, User user);
    Optional<List<Ordered>> findAllByTourAndDate_Date(Tour tour, String date);
    boolean existsByDate_DateAndDeliverPeopleAndProductBindInfosAndTour(String date, User user, ProductBindProductDetails productDetails, Tour tour);
}
