package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourRepo extends JpaRepository<Tour, Long> {
        Optional<Tour> findTourByNumber(String number);
        boolean existsByNumber(String number);
}
