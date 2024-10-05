package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepo extends JpaRepository<Card, Long> {
    Optional<Card> findByHeaderAndSubheader(String header, String subheader);
    Optional<Card> existsByHeaderAndSubheader(String header, String subheader);
}
