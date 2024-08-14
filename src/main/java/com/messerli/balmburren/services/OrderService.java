package com.messerli.balmburren.services;


import com.messerli.balmburren.entities.*;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Ordered> saveOrder(Ordered ordered);
    Optional<Ordered> putOrder(Ordered ordered);
    Optional<Ordered> deleteOrder(User user, ProductBindProductDetails productAndInfo, Dates dates, Tour tour);
    Optional<Ordered> getOrder(User user, ProductBindProductDetails productAndInfo, Dates dates, Tour tour);
    Optional<List<Ordered>> getAllOrderForPeople(User user);
    Optional<List<Ordered>> getAllOrderForPeopleBetween(String startDate, String endDate, User user);
    Optional<List<Ordered>> getAllOrderForTourAndDate(Tour tour, String date);
    boolean existOrderForDatePeopleAndProductBindInfosAndTour(String date, User user, ProductBindProductDetails productBindProductDetails, Tour tour);
    Optional<PersonProfileOrder> savePersonProfileOrder(PersonProfileOrder personProfileOrder);
    Optional<PersonProfileOrder> putPersonProfileOrder(PersonProfileOrder personProfileOrder);
    Optional<PersonProfileOrder> getPersonProfileOrder(User user, ProductBindProductDetails productBindProductDetails, Tour tour);
    Optional<PersonProfileOrder> deletePersonProfileOrder(User user, ProductBindProductDetails productBindProductDetails, Tour tour);
    boolean existPersonProfileOrder(User user, ProductBindProductDetails productBindProductDetails, Tour tour);
    Optional<List<PersonProfileOrder>> getAllPersonProfileOrderForPerson(User user);
    Optional<List<PersonProfileOrder>> getAllPersonProfileOrder();


}
