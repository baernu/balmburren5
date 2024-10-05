package com.messerli.balmburren.controllers;

import com.messerli.balmburren.entities.Card;
import com.messerli.balmburren.services.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/cd/")
@RestController
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN','KATHY')")
    @PostMapping("card/")
    ResponseEntity<Optional<Card>> createCard(@RequestBody Card card) {
        return ResponseEntity.ok().body(cardService.saveCard(card));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN','KATHY')")
    @PatchMapping("card/")
    ResponseEntity<Optional<Card>> deleteCard(@RequestBody Card card) {
        return ResponseEntity.ok().body(cardService.delete(card));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'KATHY')")
    @GetMapping("card/{header}/{subheader}")
    ResponseEntity<Optional<Card>> findCardbyHeaderAndSubheader(@PathVariable("header") String header, @PathVariable("subheader") String subheader) {
        return ResponseEntity.ok().body(cardService.findByHeaderAndSubheader(header, subheader));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN','KATHY')")
    @GetMapping("card/isactive/{isactive}")
    ResponseEntity<List<Card>> findAllActive(@PathVariable("isactive") boolean isactive) {
        return ResponseEntity.ok().body(cardService.findAllActive(isactive));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN','KATHY')")
    @GetMapping("card/exist/{header}/{subheader}")
    ResponseEntity<Boolean> existCardbyHeaderAndSubheader(@PathVariable("header") String header, @PathVariable("subheader") String subheader) {
        return ResponseEntity.ok().body(cardService.existByHeaderAndSubheader(header, subheader));}
}
