package com.messerli.balmburren.services;


import com.messerli.balmburren.entities.*;

import java.util.List;
import java.util.Optional;

public interface UserBindService {
    Optional<Address> saveAddress(Address address);
    Optional<Address> putAddress(Address address);
    Optional<Address> deleteAddress(Address address);
    Optional<Address> getAddress(Long id);
    Optional<PersonBindDeliverAddress> savePersonBindDeliverAddress(PersonBindDeliverAddress personBindDeliverAddress);
    Optional<PersonBindDeliverAddress> putPersonBindDeliverAddress(PersonBindDeliverAddress personBindDeliverAddress);
    Optional<PersonBindDeliverAddress> deletePersonBindDeliverAddress(User user);
    Optional<PersonBindDeliverAddress> getPersonBindDeliverAddress(User user);
    boolean existPersonBindDeliverAddress(User user);
    Optional<PersonBindInvoice> savePersonBindInvoice(PersonBindInvoice personBindInvoice);
    Optional<PersonBindInvoice> putPersonBindInvoice(PersonBindInvoice personBindInvoice);
    Optional<PersonBindInvoice> getPersonBindInvoice(String dateFrom, String dateTo, User peopleInvoice, User peopleDeliver);
    boolean existPersonBindInvoice(String dateFrom, String dateTo, User peopleInvoice, User peopleDeliver);
    Optional<List<PersonBindInvoice>> getAllPersonBindInvoiceForDeliver(User user);
    Optional<List<PersonBindInvoice>> getAllPersonBindInvoiceForInvoice(User user);
    Optional<List<PersonBindInvoice>> getAllPersonBindInvoiceForDateFrom(String dateFrom);
    Optional<List<PersonBindInvoice>>getAllPersonBindInvoice();
    Optional<PersonBindInvoice> deletePersonBindInvoice(String dateFrom, String dateTo, User peopleInvoice, User peopleDeliver);

    void deletePersonBindInvoiceById(PersonBindInvoice personBindInvoice);


    Optional<DriverBindInvoice> saveDriverBindInvoice(DriverBindInvoice driverBindInvoice);
    Optional<DriverBindInvoice> putDriverBindInvoice(DriverBindInvoice driverBindInvoice);
    Optional<DriverBindInvoice> getDriverBindInvoice(String dateFrom, String dateTo, User peopleInvoice);
    boolean existDriverBindInvoice(String dateFrom, String dateTo, User peopleInvoice);
    Optional<List<DriverBindInvoice>> getAllDriverBindInvoiceForInvoice(User user);
    Optional<List<DriverBindInvoice>> getAllDriverBindInvoiceForDateFrom(String dateFrom);
    Optional<List<DriverBindInvoice>>getAllDriverBindInvoice();
    Optional<DriverBindInvoice> deleteDriverBindInvoice(String dateFrom, String dateTo, User peopleInvoice);

    void deleteDriverBindInvoiceById(DriverBindInvoice driverBindInvoice);

    Optional<PersonBindPhone> savePersonBindPhone(PersonBindPhone personBindPhone);
    Optional<PersonBindPhone> putPersonBindPhone(PersonBindPhone personBindPhone);
    Optional<PersonBindPhone> getPersonBindPhone(User user);
    Optional<PersonBindPhone> deletePersonBindPhone(User user);
    boolean existPersonBindPhone(User user);
    Optional<PersonBindTour> savePersonBindTour(PersonBindTour personBindTour);
    Optional<PersonBindTour> putPersonBindTour(PersonBindTour personBindTour);
    Optional<PersonBindTour> getPersonBindTour(User user, Tour tour);
    boolean existPersonAndTour(User user, Tour tour);
    void deletePersonBindTour(Optional<PersonBindTour> personBindTour);
    Optional<List<PersonBindTour>> getAllPersonsForTour(Tour tour);


}
