package com.messerli.balmburren;


import com.messerli.balmburren.entities.PersonBindDeliverAddress;
import com.messerli.balmburren.entities.PersonBindPhone;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.responses.LoginResponse;
import com.messerli.balmburren.services.serviceImpl.FlywayServiceImpl;
import jakarta.transaction.Transactional;

import com.messerli.balmburren.entities.Address;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

//@Transactional
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BackendIntegrationTests_SettingsTest {
    @Autowired
    private WebTestClient webClient;
    @MockBean
    private FlywayServiceImpl flywayService;


    @Test
    void createAddressAndSoOn() {

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



        Address address = new Address();
        address.setStreet("11");
        address.setCity("Wohlen");
        address.setPlz(3033);

        EntityExchangeResult<Address> result1 =
                webClient.post().uri("/bd/address/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(address)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectBody(Address.class)
                        .returnResult();
        address = result1.getResponseBody();

        webClient.get().uri("/bd/address/" + address.getId())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Address.class)
                .isEqualTo(address);

        address.setCity("Thun");
        EntityExchangeResult<Address> result12 =
                webClient.put().uri("/bd/address/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(address)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectBody(Address.class)
                        .returnResult();
        address = result12.getResponseBody();


        webClient.get().uri("/bd/address/" + address.getId())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Address.class)
                .isEqualTo(address);





        PersonBindDeliverAddress personBindDeliverAddress = new PersonBindDeliverAddress();
        personBindDeliverAddress.setAddress(address);
        personBindDeliverAddress.setUser(resultUser.getResponseBody());

        EntityExchangeResult<PersonBindDeliverAddress> result2 =
                webClient.post().uri("/bd/person/bind/deliveraddress/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(personBindDeliverAddress)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectBody(PersonBindDeliverAddress.class)
                        .returnResult();
        personBindDeliverAddress = result2.getResponseBody();

        webClient.get().uri("/bd/person/bind/deliveraddress/" + resultUser.getResponseBody().getUsername())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonBindDeliverAddress.class)
                .isEqualTo(personBindDeliverAddress);

        PersonBindPhone personBindPhone = new PersonBindPhone();
        personBindPhone.setUser(resultUser.getResponseBody());
        personBindPhone.setEmail("bernhard@balmburren.net");

        EntityExchangeResult<PersonBindPhone> result3 =
                webClient.post().uri("/bd/person/bind/phone/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(personBindDeliverAddress)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectBody(PersonBindPhone.class)
                        .returnResult();
        personBindPhone = result3.getResponseBody();

        webClient.get().uri("/bd/person/bind/phone/" + resultUser.getResponseBody().getUsername())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonBindPhone.class)
                .isEqualTo(personBindPhone);



    }
}
