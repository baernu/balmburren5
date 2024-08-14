package com.messerli.balmburren.services.serviceImpl;


import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.WagePayment;
import com.messerli.balmburren.entities.Work;
import com.messerli.balmburren.repositories.WagePaymentRepo;
import com.messerli.balmburren.repositories.WorkRepo;
import com.messerli.balmburren.services.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class WorkServiceImpl implements WorkService {
    private final WorkRepo workRepo;
    private final WagePaymentRepo wagePaymentRepo;

    public WorkServiceImpl(WorkRepo workRepo, WagePaymentRepo wagePaymentRepo) {
        this.workRepo = workRepo;
        this.wagePaymentRepo = wagePaymentRepo;
    }

    @Override
    public Optional<Work> saveWork(Work work) {
        log.info("Saving new Work: {}", work);
        return Optional.of(workRepo.save(work));
    }

    @Override
    public Optional<Work> putWork(Work work) {
        log.info("Updating Work: {}", work);
        return Optional.of(workRepo.save(work));
    }

    @Override
    public Optional<Work> getWork(User user, Dates date) {
        Optional<Work> work = workRepo.findByPeopleAndDate_Date(user, date.getDate());
        log.info("Get Work: {}", work.get());
        return work;
    }

    @Override
    public Optional<Work> deleteWork(User user, Dates date) {
        Optional<Work> work = getWork(user, date);
        log.info("Deleting Work: {}", work.get());
        workRepo.delete(work);
        return work;
    }

    @Override
    public Optional<List<Work>> getAllWorksForPeople(User user) {
        Optional<List<Work>> list = workRepo.findAllByPeople(user);
        log.info("Get all Works: {} for Person: {} and startDate: {} and endDate: {}",list.get(), user);
        return list;
    }

    @Override
    public Optional<WagePayment> saveWagePayment(WagePayment wagePayment) {
        log.info("Saving WagePayment: {}", wagePayment);
        return Optional.of(wagePaymentRepo.save(wagePayment));
    }

    @Override
    public Optional<WagePayment> putWagePayment(WagePayment wagePayment) {
        log.info("Updating WagePayment: {}", wagePayment);
        return Optional.of(wagePaymentRepo.save(wagePayment));
    }

    @Override
    public Optional<WagePayment> getWagePayment(User user, Dates date) {
        Optional<WagePayment> wagePayment = wagePaymentRepo.findByPersonAndDateTo_Date(user, date.getDate());
        log.info("Get WagePayment: {}", wagePayment.get());
        return wagePayment;
    }

    @Override
    public Optional<WagePayment> deleteWagePayment(User user, Dates date) {
        Optional<WagePayment> wagePayment = getWagePayment(user, date);
        log.info("Deleted WagePayment: {}", wagePayment.get());
        wagePaymentRepo.delete(wagePayment);
        return wagePayment;
    }

    @Override
    public Optional<List<WagePayment>> getAllWagePaymentsForPeople(User user) {
        Optional<List<WagePayment>> list = wagePaymentRepo.findAllByPerson(user);
        log.info("Get all WagePayments: {} for people: {}", list.get(), user);
        return list;
    }
}
