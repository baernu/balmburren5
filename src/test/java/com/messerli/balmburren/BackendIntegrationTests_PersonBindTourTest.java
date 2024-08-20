package com.messerli.balmburren;


import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.responses.LoginResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;


@Transactional
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BackendIntegrationTests_PersonBindTourTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    void ecreateUserAndSoOn() {

        EntityExchangeResult<LoginResponse> loginResponse =
                webClient.post().uri("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"password\": \"adminadmin\", \"username\": \"admin\" }")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(LoginResponse.class)
                        .returnResult();

        String token = loginResponse.getResponseBody().getToken();
        String finalToken = token;

        EntityExchangeResult<User> resultUser =
                webClient.get().uri("/users/admin")
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(User.class)
                        .returnResult();


        Assertions.assertEquals("Normal", resultUser.getResponseBody().getFirstname());

        Dates dates = new Dates();
        dates.setDate("20-08-2023");
        EntityExchangeResult<Dates> result1 =
        webClient.post().uri("/tr/dates/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dates)
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectBody(Dates.class)
                .returnResult();
        dates = result1.getResponseBody();

        Tour tour = new Tour();
        tour.setNumber("1");
        EntityExchangeResult<Tour> result2 =
        webClient.post().uri("/tr/tour/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tour)
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectBody(Tour.class)
                .returnResult();

        tour = result2.getResponseBody();

        List<Tour> tourList = new ArrayList<>();
        tourList.add(tour);
        webClient.get().uri("/tr/tour/")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Tour.class)
                .isEqualTo(tourList);

        TourBindDates tourBindDates = new TourBindDates();
        tourBindDates.setTour(tour);
        tourBindDates.setDates(dates);

        EntityExchangeResult<TourBindDates> result3 =
                webClient.post().uri("/tr/tour/bind/dates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tourBindDates)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectBody(TourBindDates.class)
                        .returnResult();

        tourBindDates = result3.getResponseBody();


        PersonBindTour personBindTour = new PersonBindTour();
        personBindTour.setUser(resultUser.getResponseBody());
        personBindTour.setTour(tour);
        EntityExchangeResult<PersonBindTour> result4 =
                webClient.post().uri("/bd/person/bind/tour/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(personBindTour)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(PersonBindTour.class)
                        .returnResult();

        personBindTour = result4.getResponseBody();

        webClient.get().uri("/bd/person/bind/tour/admin/1")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonBindTour.class)
                .isEqualTo(personBindTour);

        boolean bool = true;
        webClient.get().uri("/bd/person/bind/tour/exist/admin/1")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(bool);

        List<PersonBindTour> personBindTourList = new ArrayList<PersonBindTour>();
        personBindTourList.add(personBindTour);

        webClient.get().uri("/bd/person/bind/tour/1")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PersonBindTour.class)
                .isEqualTo(personBindTourList);

        webClient.delete().uri("/bd/person/bind/tour/admin/1")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk();

        boolean bool1 = false;
        webClient.get().uri("/bd/person/bind/tour/exist/admin/1")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(bool1);
    }

}
