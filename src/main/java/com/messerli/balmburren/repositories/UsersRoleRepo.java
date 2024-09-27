package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.Role;
import com.messerli.balmburren.entities.Tour;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.UsersRole;
import org.springframework.data.jpa.repository.JpaRepository;
import com.messerli.balmburren.repositories.UsersRoleRepo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UsersRoleRepo extends JpaRepository<UsersRole, Long> {
    Optional<UsersRole> findByUserAndRole(User user, Role role);
    List<UsersRole> findAllByUser(User user);
}
