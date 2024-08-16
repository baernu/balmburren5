package com.messerli.balmburren.services.serviceImpl;


import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.repositories.OrderedRepo;
import com.messerli.balmburren.repositories.PersonProfileOrderRepo;
import com.messerli.balmburren.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderedRepo orderedRepo;
    private final PersonProfileOrderRepo personProfileOrderRepo;

    public OrderServiceImpl(OrderedRepo orderedRepo, PersonProfileOrderRepo personProfileOrderRepo) {
        this.orderedRepo = orderedRepo;
        this.personProfileOrderRepo = personProfileOrderRepo;
    }

    @Override
    public Optional<Ordered> saveOrder(Ordered ordered) {
        log.info("Saving new Order: {}", ordered);
        return Optional.of(orderedRepo.save(ordered));
    }

    @Override
    public Optional<Ordered> putOrder(Ordered ordered) {
        log.info("Updating Order: {}", ordered);
        return Optional.of(orderedRepo.save(ordered));
    }

    @Override
    public Optional<Ordered> deleteOrder(User user, ProductBindProductDetails productAndInfo, Dates dates, Tour tour) {
        Optional<Ordered> ordered = getOrder(user, productAndInfo, dates, tour);
        log.info("Deleting Order: {}", ordered.get());
        orderedRepo.delete(ordered.get());
        return ordered;
    }

    @Override
    public Optional<Ordered> getOrder(User user, ProductBindProductDetails productAndInfo, Dates dates, Tour tour) {
        Optional<Ordered> ordered = orderedRepo.findByDeliverPeopleAndProductBindInfosAndDate_DateAndTour(user, productAndInfo, dates.getDate(), tour);
        log.info("Get Order: {}", ordered.get());
        return ordered;
    }

    @Override
    public Optional<List<Ordered>> getAllOrderForPeople(User user) {
        log.info("Get all Order for Person: {}", user.getUsername());
        return orderedRepo.findAllByDeliverPeople(user);
    }

    @Override
    public Optional<List<Ordered>> getAllOrderForPeopleBetween(String startDate, String endDate, User user) {
        log.info("Get all Order for Person: {}, dateFrom:{}, dateTo: {}", user, startDate, endDate);
        return orderedRepo.findAllByDate_DateBetweenAndDeliverPeople(startDate, endDate, user);
    }

    @Override
    public Optional<List<Ordered>> getAllOrderForTourAndDate(Tour tour, String date) {
        Optional<List<Ordered>> list = orderedRepo.findAllByTourAndDate_Date(tour, date);
        log.info("Get all Order: {} for Tour: {} and Date: {}", list.get(), tour, date);
        return list;
    }

    @Override
    public boolean existOrderForDatePeopleAndProductBindInfosAndTour(String date, User user, ProductBindProductDetails productBindProductDetails, Tour tour) {
        boolean bool = orderedRepo.existsByDate_DateAndDeliverPeopleAndProductBindInfosAndTour(date, user, productBindProductDetails, tour);
        log.info("Order exist: {}", bool);
        return bool;
    }

    @Override
    public Optional<PersonProfileOrder> savePersonProfileOrder(PersonProfileOrder personProfileOrder) {
        log.info("Saving PersonProfileOrder: {}", personProfileOrder);
        return Optional.of(personProfileOrderRepo.save(personProfileOrder));
    }

    @Override
    public Optional<PersonProfileOrder> putPersonProfileOrder(PersonProfileOrder personProfileOrder) {
        log.info("Updating PersonProfileOrder: {}", personProfileOrder);
        return Optional.of(personProfileOrderRepo.save(personProfileOrder));
    }

    @Override
    public Optional<PersonProfileOrder> getPersonProfileOrder(User user, ProductBindProductDetails productBindProductDetails, Tour tour) {
        Optional<PersonProfileOrder> personProfileOrder = personProfileOrderRepo.findByUserAndProductBindProductDetailsAndTour(user, productBindProductDetails, tour);
        log.info("Get PersonProfileOrder: {}", personProfileOrder.get());
        return personProfileOrder;
    }

    @Override
    public Optional<PersonProfileOrder> deletePersonProfileOrder(User user, ProductBindProductDetails productBindProductDetails, Tour tour) {
        Optional<PersonProfileOrder> personProfileOrder = personProfileOrderRepo.findByUserAndProductBindProductDetailsAndTour(user, productBindProductDetails, tour);
        log.info("Delete PersonProfileOrder: {}", personProfileOrder.get());
        personProfileOrderRepo.delete(personProfileOrder.get());
        return personProfileOrder;
    }

    @Override
    public boolean existPersonProfileOrder(User user, ProductBindProductDetails productBindProductDetails, Tour tour) {
        boolean bool = personProfileOrderRepo.existsByUserAndProductBindProductDetailsAndTour(user, productBindProductDetails, tour);
        log.info("PersonProfileOrder exist: {}", bool);
        return bool;
    }

    @Override
    public Optional<List<PersonProfileOrder>> getAllPersonProfileOrderForPerson(User user) {
        Optional<List<PersonProfileOrder>> list = personProfileOrderRepo.findAllByUser(user);
        log.info("Get all PersonProfileOrder: {} for Person: {}", list.get(), user);
        return list;
    }

    @Override
    public Optional<List<PersonProfileOrder>> getAllPersonProfileOrder() {
        List<PersonProfileOrder> list = personProfileOrderRepo.findAll();
        log.info("Get all PersonProfileOrder: {}", list);
        return Optional.of(list);
    }


}
