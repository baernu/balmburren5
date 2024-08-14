package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.Dates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DatesRepo extends JpaRepository<Dates, Long> {
    Optional<Dates> findById(Long id);
    boolean existsByDate(String date);
}

