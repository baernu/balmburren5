package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.Back;
import com.messerli.balmburren.entities.Dates;
import com.messerli.balmburren.entities.ProductBindProductDetails;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BackRepo extends JpaRepository<Back, Long> {
    Optional<Back> findBackByDeliverPeopleAndProductBindInfosAndDate(User user, ProductBindProductDetails productAndInfo, Dates date);
    Optional<List<Back>> findAllByDeliverPeople(User user);
}
