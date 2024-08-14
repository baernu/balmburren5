package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.PersonBindDeliverAddress;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonBindDeliverAddressRepo extends JpaRepository<PersonBindDeliverAddress, Long> {
    Optional<PersonBindDeliverAddress> findByPerson(User user);
    boolean existsByPerson(User user);

    void delete(Optional<PersonBindDeliverAddress> personBindDeliverAddress);
}
