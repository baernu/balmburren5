package com.messerli.balmburren.services;

import com.messerli.balmburren.entities.Dates;

import java.util.Optional;

public interface DatesService {
    Optional<Dates> saveDate(Dates dates);
    Optional<Dates> getDate(Long id);
    boolean existDate(String date);
}
