package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.WagePayment;
import com.messerli.balmburren.entities.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WagePaymentRepo extends JpaRepository<WagePayment, Long> {
    Optional<WagePayment> findByUserAndDateTo_Date(User user, String date);
    Optional<List<WagePayment>> findAllByUser(User user);
    Optional<List<WagePayment>> findAllByDates_DateFromBetweenAndUser(Dates startDate, Dates endDate, User user);

}
