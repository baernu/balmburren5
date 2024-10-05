package com.messerli.balmburren.services;

import com.messerli.balmburren.entities.Card;

import java.util.Optional;

public interface CardService {
    Optional<Card> findByHeaderAndSubheader(String header, String subheader);
    Optional<Card> existByHeaderAndSubheader(String header, String subheader);

    Optional<Card> saveCard(Card card);

    Optional<Card> delete(Card card);
}
