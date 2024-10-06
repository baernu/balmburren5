package com.messerli.balmburren.services;

import com.messerli.balmburren.entities.Card;

import java.util.List;
import java.util.Optional;

public interface CardService {
    Optional<Card> findByHeaderAndSubheader(String header, String subheader);

    Optional<Card> findById(Long id);

    List<Card> findAllActive(boolean isactive);

    boolean existByHeaderAndSubheader(String header, String subheader);

    Optional<Card> saveCard(Card card);

    Optional<Card> delete(Card card);

    List<Card> findAll();
}
