package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.entities.Back;
import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.ProductBindProductDetails;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.repositories.BackRepo;
import com.messerli.balmburren.services.BackService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;


@Service
@Transactional
@Slf4j
public class BackServiceImpl implements BackService {
    private final BackRepo backRepo;

    public BackServiceImpl(BackRepo backRepo) {
        this.backRepo = backRepo;
    }

    @Override
    public Optional<Back> saveBack(Back back) {
        log.info("Saving Back with name: {} with date: {}, from Person: {}", back.getProductBindInfos().getProduct().getName(),
                back.getDate(), back.getDeliverPeople().getUsername());
        return Optional.of(backRepo.save(back));
    }

    @Override
    public Optional<Back> putBack(Back back) {
        log.info("Updating Back with name: {} with date: {}, from Person: {}", back.getProductBindInfos().getProduct().getName(),
                back.getDate(), back.getDeliverPeople().getUsername());
        return Optional.of(backRepo.save(back));
    }

    @Override
    public Optional<Back> deleteBack(User user, ProductBindProductDetails productAndInfo, Dates date) {
        Optional<Back> back = backRepo.findBackByDeliverPeopleAndProductBindInfosAndDate(user, productAndInfo, date);
        log.info("Deleting Back with name: {} with date: {}, from Person: {}", back.get().getProductBindInfos().getProduct().getName(),
                back.get().getDate(), back.get().getDeliverPeople().getUsername());
        backRepo.delete(back.get());
        return back;
    }

    @Override
    public Optional<Back> getBack(User user, ProductBindProductDetails productAndInfo, Dates date) {
        Optional<Back> back = backRepo.findBackByDeliverPeopleAndProductBindInfosAndDate(user, productAndInfo, date);
        log.info("Get Back with name: {} with date: {}, from Person: {}", back.get().getProductBindInfos().getProduct().getName(),
                back.get().getDate(), back.get().getDeliverPeople().getUsername());
        return back;
    }

    @Override
    public Optional<List<Back>> getAllBackForPeople(User user) {
        log.info("Get all Back for people: {}", user.getUsername());
        return backRepo.findAllByDeliverPeople(user);
    }
}
