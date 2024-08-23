package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReferenceRepo extends JpaRepository<Reference, Long> {
    Optional<Reference> findByVal(Long val);
}
