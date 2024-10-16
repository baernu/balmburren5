package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.repositories.*;
import com.messerli.balmburren.services.UserBindService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Slf4j
public class UserBindServiceImpl implements UserBindService {
    private final PersonBindTourRepo personBindTourRepo;
    private final PersonBindDeliverAddressRepo personBindDeliverAddressRepo;
    private final AddressRepo addressRepo;
    private final PersonBindInvoiceRepo personBindInvoiceRepo;
    private final PersonBindPhoneRepo personBindPhoneRepo;
    private final DriverBindInvoiceRepo driverBindInvoiceRepo;
    @Autowired
    private final UsersRoleRepo usersRoleRepo;

    public UserBindServiceImpl(PersonBindTourRepo personBindTourRepo, PersonBindDeliverAddressRepo personBindDeliverAddressRepo, AddressRepo addressRepo, PersonBindInvoiceRepo personBindInvoiceRepo, PersonBindPhoneRepo personBindPhoneRepo, DriverBindInvoiceRepo driverBindInvoiceRepo, UsersRoleRepo usersRoleRepo) {
        this.personBindTourRepo = personBindTourRepo;
        this.personBindDeliverAddressRepo = personBindDeliverAddressRepo;
        this.addressRepo = addressRepo;
        this.personBindInvoiceRepo = personBindInvoiceRepo;
        this.personBindPhoneRepo = personBindPhoneRepo;
        this.driverBindInvoiceRepo = driverBindInvoiceRepo;
        this.usersRoleRepo = usersRoleRepo;
    }

    @Override
    public Optional<Address> saveAddress(Address address) {
        log.info("Saving Address: {}", address);
        return Optional.of(addressRepo.save(address));
    }

    @Override
    public Optional<Address> putAddress(Address address) {
        log.info("Updating Address: {}", address);
        return Optional.of(addressRepo.save(address));
    }

    @Override
    public Optional<Address> deleteAddress(Address address) {
        log.info("Deleted Address: {}", address);
        Optional<Address> address1 = getAddress(address.getId());
        addressRepo.delete(address1.get());
        return Optional.of(address);
    }

    @Override
    public Optional<Address> getAddress(Long id) {
        Optional<Address> address = addressRepo.findById(id);
        log.info("Get Address: {}", address);
        return address;
    }

    @Override
    public Optional<PersonBindDeliverAddress> savePersonBindDeliverAddress(PersonBindDeliverAddress personBindDeliverAddress) {
        log.info("Saving PersonBindDeliverAddress: {}", personBindDeliverAddress);
        return Optional.of(personBindDeliverAddressRepo.save(personBindDeliverAddress));
    }

    @Override
    public Optional<PersonBindDeliverAddress> putPersonBindDeliverAddress(PersonBindDeliverAddress personBindDeliverAddress) {
        log.info("Updating PersonBindDeliverAddress: {}", personBindDeliverAddress);
        return Optional.of(personBindDeliverAddressRepo.save(personBindDeliverAddress));
    }

    @Override
    public Optional<PersonBindDeliverAddress> deletePersonBindDeliverAddress(User user) {
        Optional<PersonBindDeliverAddress> personBindDeliverAddress = getPersonBindDeliverAddress(user);
        log.info("Deleted PersonBindDeliverAddress: {}", personBindDeliverAddress);
        personBindDeliverAddressRepo.delete(personBindDeliverAddress.get());
        return personBindDeliverAddress;
    }

    @Override
    public Optional<PersonBindDeliverAddress> getPersonBindDeliverAddress(User user) {
        Optional<PersonBindDeliverAddress> personBindDeliverAddress = personBindDeliverAddressRepo.findByUser(user);
        log.info("Get PersonBindDeliverAddress: {}", personBindDeliverAddress.get());
        return personBindDeliverAddress;
    }

    @Override
    public boolean existPersonBindDeliverAddress(User user) {
        boolean bool = personBindDeliverAddressRepo.existsByUser(user);
        log.info("PersonBindDeliverAddress for Person: {} exist: {}", user, bool);
        return bool;
    }

    @Override
    public Optional<PersonBindInvoice> savePersonBindInvoice(PersonBindInvoice personBindInvoice) {
        log.info("Saving PersonBindInvoice: {}", personBindInvoice);
        return Optional.of(personBindInvoiceRepo.save(personBindInvoice));
    }

    @Override
    public Optional<PersonBindInvoice> putPersonBindInvoice(PersonBindInvoice personBindInvoice) {
        log.info("Updating PersonBindInvoice: {}", personBindInvoice);
        return Optional.of(personBindInvoiceRepo.save(personBindInvoice));
    }

    @Override
    public Optional<PersonBindInvoice> getPersonBindInvoice(String dateFrom, String dateTo, User peopleInvoice, User peopleDeliver) {
        Optional<PersonBindInvoice> personBindInvoice = personBindInvoiceRepo.findByDateFrom_DateAndDateTo_DateAndPersonInvoiceAndPersonDeliver(dateFrom, dateTo, peopleInvoice, peopleDeliver);
        log.info("Get PersonBindInvoice: {}", personBindInvoice.get());
        return personBindInvoice;
    }

    @Override
    public boolean existPersonBindInvoice(String dateFrom, String dateTo, User peopleInvoice, User peopleDeliver) {
        boolean bool = personBindInvoiceRepo.existsByDateFrom_DateAndDateTo_DateAndPersonInvoiceAndPersonDeliver(dateFrom, dateTo, peopleInvoice, peopleDeliver);
        log.info("Exist PersonBindInvoice: {}", bool);
        return bool;
    }

    @Override
    public Optional<List<PersonBindInvoice>> getAllPersonBindInvoiceForDeliver(User user) {
        Optional<List<PersonBindInvoice>> list = personBindInvoiceRepo.getAllByPersonDeliver(user);
        log.info("Get all PersonBindInvoice: {} for people deliver: {}", list.get(), user);
        return list;
    }

    @Override
    public Optional<List<PersonBindInvoice>> getAllPersonBindInvoiceForInvoice(User user) {
        Optional<List<PersonBindInvoice>> list = personBindInvoiceRepo.getAllByPersonInvoice(user);
        log.info("Get all PersonBindInvoice: {} for people invoice: {}", list.get(), user);
        return list;
    }

    @Override
    public Optional<List<PersonBindInvoice>> getAllPersonBindInvoiceForDateFrom(String dateFrom) {
        Optional<List<PersonBindInvoice>> list = personBindInvoiceRepo.getAllByDateFrom_Date(dateFrom);
        log.info("Get all PersonBindInvoice: {}", list.get());
        return list;
    }

    @Override
    public Optional<List<PersonBindInvoice>> getAllPersonBindInvoiceForDateTo(String dateTo) {
        Optional<List<PersonBindInvoice>> list = personBindInvoiceRepo.getAllByDateTo_Date(dateTo);
        log.info("Get all PersonBindInvoice: {}", list.get());
        return list;
    }

    @Override
    public Optional<List<PersonBindInvoice>> getAllPersonBindInvoice() {
        List<PersonBindInvoice> list = personBindInvoiceRepo.findAll();
        log.info("Get all PersonBindInvoice: {}", list);
        return Optional.of(list);
    }

    @Override
    public Optional<PersonBindInvoice> deletePersonBindInvoice(String dateFrom, String dateTo, User peopleInvoice, User peopleDeliver) {
        Optional<PersonBindInvoice> personBindInvoice = getPersonBindInvoice(dateFrom, dateTo, peopleInvoice, peopleDeliver);
        log.info("Deleted PersonBindInvoice: {}", personBindInvoice);
        personBindInvoiceRepo.delete(personBindInvoice.get());
        return personBindInvoice;
    }

    @Override
    public void deletePersonBindInvoiceById(PersonBindInvoice personBindInvoice) {
        personBindInvoiceRepo.delete(personBindInvoice);
        log.info("Deleted PersonBindInvoice: {}", personBindInvoice);
    }

    @Override
    public Optional<DriverBindInvoice> saveDriverBindInvoice(DriverBindInvoice driverBindInvoice) {
        log.info("Saving DriverBindInvoice: {}", driverBindInvoice);
        return Optional.of(driverBindInvoiceRepo.save(driverBindInvoice));
    }

    @Override
    public Optional<DriverBindInvoice> putDriverBindInvoice(DriverBindInvoice driverBindInvoice) {
        log.info("Updating DriverBindInvoice: {}", driverBindInvoice);
        return Optional.of(driverBindInvoiceRepo.save(driverBindInvoice));
    }

    @Override
    public Optional<DriverBindInvoice> getDriverBindInvoice(String dateFrom, String dateTo, User peopleInvoice) {
        Optional<DriverBindInvoice> driverBindInvoice = driverBindInvoiceRepo.findByDateFrom_DateAndDateTo_DateAndPersonInvoice(dateFrom, dateTo, peopleInvoice);
        log.info("Get DriverBindInvoice: {}", driverBindInvoice.get());
        return driverBindInvoice;
    }

    @Override
    public boolean existDriverBindInvoice(String dateFrom, String dateTo, User peopleInvoice) {
        boolean bool = driverBindInvoiceRepo.existsByDateFrom_DateAndDateTo_DateAndPersonInvoice(dateFrom, dateTo, peopleInvoice);
        log.info("Exist DriverBindInvoice: {}", bool);
        return bool;
    }

    @Override
    public Optional<List<DriverBindInvoice>> getAllDriverBindInvoiceForInvoice(User user) {
        Optional<List<DriverBindInvoice>> list = driverBindInvoiceRepo.getAllByPersonInvoice(user);
        log.info("Get all DriverBindInvoice: {} for people invoice: {}", list.get(), user);
        return list;
    }

    @Override
    public Optional<List<DriverBindInvoice>> getAllDriverBindInvoiceForDateFrom(String dateFrom) {
        Optional<List<DriverBindInvoice>> list = driverBindInvoiceRepo.getAllByDateFrom_Date(dateFrom);
        log.info("Get all DriverBindInvoice: {} for people invoice: {}", list.get());
        return list;
    }

    @Override
    public Optional<List<DriverBindInvoice>> getAllDriverBindInvoice() {
        List<DriverBindInvoice> list = driverBindInvoiceRepo.findAll();
        log.info("Get all DriverBindInvoice: {}", list);
        return Optional.of(list);
    }

    @Override
    public Optional<DriverBindInvoice> deleteDriverBindInvoice(String dateFrom, String dateTo, User peopleInvoice) {
        Optional<DriverBindInvoice> driverBindInvoice = getDriverBindInvoice(dateFrom, dateTo, peopleInvoice);
        log.info("Deleted DriverBindInvoice: {}", driverBindInvoice);
        driverBindInvoiceRepo.delete(driverBindInvoice.get());
        return driverBindInvoice;
    }

    @Override
    public void deleteDriverBindInvoiceById(DriverBindInvoice driverBindInvoice) {
        driverBindInvoiceRepo.delete(driverBindInvoice);
        log.info("Deleted DriverBindInvoice: {}", driverBindInvoice);
    }

    @Override
    public Optional<PersonBindPhone> savePersonBindPhone(PersonBindPhone personBindPhone) {
        log.info("Saving PersonBindPhone: {}", personBindPhone);
        return Optional.of(personBindPhoneRepo.save(personBindPhone));
    }

    @Override
    public Optional<PersonBindPhone> putPersonBindPhone(PersonBindPhone personBindPhone) {
        log.info("Updating PersonBindPhone: {}", personBindPhone);
        return Optional.of(personBindPhoneRepo.save(personBindPhone));
    }

    @Override
    public Optional<PersonBindPhone> getPersonBindPhone(User user) {
        Optional<PersonBindPhone> personBindPhone = personBindPhoneRepo.findByUser(user);
        log.info("Get PersonBindPhone: {}", personBindPhone.get());
        return personBindPhone;
    }

    @Override
    public Optional<PersonBindPhone> deletePersonBindPhone(User user) {
        Optional<PersonBindPhone> personBindPhone = getPersonBindPhone(user);
        log.info("Deleted PersonBindPhone: {}", personBindPhone.get());
        personBindPhoneRepo.delete(personBindPhone.get());
        return personBindPhone;
    }

    @Override
    public boolean existPersonBindPhone(User user) {
        boolean bool = personBindPhoneRepo.existsByUser(user);
        log.info("PersonBindPhone for Person: {} exist: {}", user, bool);
        return bool;
    }

    @Override
    public Optional<PersonBindTour> savePersonBindTour(PersonBindTour personBindTour) {
        log.info("Saving PersonBindTour: {}", personBindTour);
        return Optional.of(personBindTourRepo.save(personBindTour));
    }

    @Override
    public Optional<PersonBindTour> putPersonBindTour(PersonBindTour personBindTour) {
        log.info("Updating PersonBindTour: {}", personBindTour);
        return Optional.of(personBindTourRepo.save(personBindTour));
    }

    @Override
    public Optional<PersonBindTour> getPersonBindTour(User user, Tour tour) {
        Optional<PersonBindTour> personBindTour = personBindTourRepo.findByUserAndTour(user, tour);
        log.info("Get PersonBindTour: {}", personBindTour.get() );
        return personBindTour;
    }

    @Override
    public boolean existPersonAndTour(User user, Tour tour) {
        boolean isExist = personBindTourRepo.existsByUserAndTour(user, tour);
        log.info("For the tour: {}, there is for person: {} a value: {}", tour, user, isExist);
        return isExist;
    }

    @Override
    public void deletePersonBindTour(Optional<PersonBindTour> personBindTour) {
        log.info("Deleting PersonBindTour: {}", personBindTour);
        personBindTourRepo.delete(personBindTour.get());
    }


    @Override
    public Optional<List<PersonBindTour>> getAllPersonsForTour(Tour tour) {
        Optional<List<PersonBindTour>> list = personBindTourRepo.findAllByTour(tour);
        log.info("Get all Persons: {} for tour: {}", list.get(), tour);
        return list;
    }

    @Override
    public List<UsersRole> getAllUserBindRoles(User user) {
//        List<UsersRole> usersRoles = new ArrayList<>();
//
//        usersRoleRepo.findAllByUser(user).forEach(usersRoles::add);
//        log.info("Get all UserBindRoles: {} ", usersRoles);
//        return usersRoles ;

        return usersRoleRepo.findAllByUser(user);
    }

    @Override
    public Optional<UsersRole> savePersonBindRole(UsersRole usersRole) {
        return Optional.of(usersRoleRepo.save(usersRole));
    }

    @Override
    public void deletePersonBindRole(UsersRole usersRole) {
        usersRoleRepo.delete(usersRole);
    }

}
