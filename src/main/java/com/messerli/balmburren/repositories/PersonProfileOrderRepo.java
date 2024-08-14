package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.PersonProfileOrder;
import com.messerli.balmburren.entities.ProductBindProductDetails;
import com.messerli.balmburren.entities.Tour;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonProfileOrderRepo extends JpaRepository<PersonProfileOrder, Long> {
    Optional<PersonProfileOrder> findByPersonAndProductBindProductDetailsAndTour(User user, ProductBindProductDetails productBindProductDetails, Tour tour);
    boolean existsByPersonAndProductBindProductDetailsAndTour(User user, ProductBindProductDetails productBindProductDetails, Tour tour);
    Optional<List<PersonProfileOrder>> findAllByUser(User user);

//    void delete(PersonProfileOrder personProfileOrder);

}
