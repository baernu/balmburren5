package com.messerli.balmburren.services;


import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.WagePayment;
import com.messerli.balmburren.entities.Work;

import java.util.List;
import java.util.Optional;

public interface WorkService {
    Optional<Work> saveWork(Work work);
    Optional<Work> putWork(Work work);
    Optional<Work> getWork(User user, Dates date);
    Optional<Work> deleteWork(User user, Dates date);
    Optional<List<Work>> getAllWorksForPeople(User user);

    Optional<List<Work>> getAllWorksForPeopleAndIntervall(User user, Dates startDate, Dates endDate);

    Optional<WagePayment> saveWagePayment(WagePayment wagePayment);
    Optional<WagePayment> putWagePayment(WagePayment wagePayment);
    Optional<WagePayment> getWagePayment(User user, Dates date);
    Optional<WagePayment> deleteWagePayment(User user, Dates date);
    Optional<List<WagePayment>> getAllWagePaymentsForPeople(User user);

    Optional<List<WagePayment>> getAllWagePaymentsForPeopleAndIntervall(User user, Dates startDate, Dates endDate);
}
