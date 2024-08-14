package com.messerli.balmburren.repositories;


import com.messerli.balmburren.entities.PersonBindPhone;
import com.messerli.balmburren.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonBindPhoneRepo extends JpaRepository<PersonBindPhone, Long> {
    Optional<PersonBindPhone> findByPerson(User user);
    boolean existsByPerson(User user);

    void delete(Optional<PersonBindPhone> personBindPhone);
}
