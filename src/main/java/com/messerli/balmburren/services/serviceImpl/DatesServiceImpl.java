package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.repositories.DatesRepo;
import com.messerli.balmburren.services.DatesService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
@Slf4j
public class DatesServiceImpl implements DatesService {
    private final DatesRepo datesRepo;

    public DatesServiceImpl(DatesRepo datesRepo) {
        this.datesRepo = datesRepo;
    }

    @Override
    public Optional<Dates> saveDate(Dates date) {
        log.info("Saving new Dates with Date: {} to the database", date.getDate());
        return Optional.of(datesRepo.save(date));
    }

    public Optional<Dates> getDate(Long id) {
        Dates dates = datesRepo.findById(id).get();
        log.info("Dates returned with date: {}", dates);
        return Optional.of(dates);
    }

    @Override
    public boolean existDate(String date) {
        boolean bool = datesRepo.existsByDate(date);
        log.info("Dates exists: {}", bool);
        return bool;
    }
}
