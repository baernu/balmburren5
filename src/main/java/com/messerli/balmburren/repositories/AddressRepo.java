package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AddressRepo extends JpaRepository<Address, Long> {
    Optional<Address> findById(Long id);
}
