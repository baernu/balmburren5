package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.PersonBindDeliverAddress;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonBindDeliverAddressRepo extends JpaRepository<PersonBindDeliverAddress, Long> {
    Optional<PersonBindDeliverAddress> findByUser(User user);
    boolean existsByUser(User user);

}
