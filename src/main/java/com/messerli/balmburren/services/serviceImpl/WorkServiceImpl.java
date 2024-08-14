package com.messerli.balmburren.services.serviceImpl;

import com.messerli.burren.domain.Dates;
import com.messerli.burren.domain.People;
import com.messerli.burren.domain.WagePayment;
import com.messerli.burren.domain.Work;
import com.messerli.burren.repos.WagePaymentRepo;
import com.messerli.burren.repos.WorkRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WorkServiceImpl implements WorkService{
    private final WorkRepo workRepo;
    private final WagePaymentRepo wagePaymentRepo;
    @Override
    public Work saveWork(Work work) {
        log.info("Saving new Work: {}", work);
        return workRepo.save(work);
    }

    @Override
    public Work putWork(Work work) {
        log.info("Updating Work: {}", work);
        return workRepo.save(work);
    }

    @Override
    public Work getWork(People people, Dates date) {
        Work work = workRepo.findByPeopleAndDate_Date(people, date.getDate());
        log.info("Get Work: {}", work);
        return work;
    }

    @Override
    public Work deleteWork(People people, Dates date) {
        Work work = getWork(people, date);
        log.info("Deleting Work: {}", work);
        workRepo.delete(work);
        return work;
    }

    @Override
    public List<Work> getAllWorksForPeople(People people) {
        List<Work> list = workRepo.findAllByPeople(people);
        log.info("Get all Works: {} for Person: {} and startDate: {} and endDate: {}",list, people);
        return list;
    }

    @Override
    public WagePayment saveWagePayment(WagePayment wagePayment) {
        log.info("Saving WagePayment: {}", wagePayment);
        return wagePaymentRepo.save(wagePayment);
    }

    @Override
    public WagePayment putWagePayment(WagePayment wagePayment) {
        log.info("Updating WagePayment: {}", wagePayment);
        return wagePaymentRepo.save(wagePayment);
    }

    @Override
    public WagePayment getWagePayment(People people, Dates date) {
        WagePayment wagePayment = wagePaymentRepo.findByPersonAndDateTo_Date(people, date.getDate());
        log.info("Get WagePayment: {}", wagePayment);
        return wagePayment;
    }

    @Override
    public WagePayment deleteWagePayment(People people, Dates date) {
        WagePayment wagePayment = getWagePayment(people, date);
        log.info("Deleted WagePayment: {}", wagePayment);
        wagePaymentRepo.delete(wagePayment);
        return wagePayment;
    }

    @Override
    public List<WagePayment> getAllWagePaymentsForPeople(People people) {
        List<WagePayment> list = wagePaymentRepo.findAllByPerson(people);
        log.info("Get all WagePayments: {} for people: {}", list, people);
        return list;
    }
}
