package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.entities.Card;
import com.messerli.balmburren.repositories.CardRepo;
import com.messerli.balmburren.services.CardService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Transactional
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepo cardRepo;

    public CardServiceImpl(CardService cardService, CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    @Override
    public Optional<Card> findByHeaderAndSubheader(String header, String subheader) {

        return cardRepo.findByHeaderAndSubheader(header, subheader);
    }

    @Override
    public Optional<Card> existByHeaderAndSubheader(String header, String subheader) {

        return cardRepo.existsByHeaderAndSubheader(header, subheader);
    }

    @Override
    public Optional<Card> saveCard(Card card) {

        return Optional.of(cardRepo.save(card));
    }

    @Override
    public Optional<Card> delete(Card card) {
        cardRepo.delete(card);
        return Optional.of(card);
    }
}
