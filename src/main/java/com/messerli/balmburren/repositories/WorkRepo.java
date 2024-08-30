package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WorkRepo extends JpaRepository<Work, Long> {
    Optional<Work> findByUserAndDate_Date(User user, String date);

    Optional<List<Work>> findAllByUser(User user);

    Optional<List<Work>> findAllByDates_DateBetweenAndUser(Dates startDate, Dates endDate, User user);
}