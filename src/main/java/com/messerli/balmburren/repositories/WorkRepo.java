package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WorkRepo extends JpaRepository<Work, Long> {
    Optional<Work> findByPeopleAndDate_Date(User user, String date);

    Optional<List<Work>> findAllByPeople(User user);
}