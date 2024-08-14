package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.PersonBindTour;
import com.messerli.balmburren.entities.Tour;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonBindTourRepo extends JpaRepository<PersonBindTour, Long> {
    Optional<PersonBindTour> findByUserAndTour(User user, Tour tour);
    Optional<List<PersonBindTour>> findAllByTour(Tour tour);
    boolean existsByUserAndTour(User user, Tour tour);
}
