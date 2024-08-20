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

import java.util.ArrayList;
import java.util.List;

@Transactional
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BackendIntegrationTests_OrderTest {
    @Autowired
    private WebTestClient webClient;


    @Test
    void postOrder() {
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


        Product product = new Product();
        product.setName("milk");
        EntityExchangeResult<Product> result1 =
                webClient.post().uri("/pr/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(product)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(Product.class)
                        .returnResult();
        product = result1.getResponseBody();


        List<Product> list = new ArrayList<>();
        list.add(product);
        webClient.get().uri("/pr/product/")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Product.class)
                .isEqualTo(list);
        boolean bool1 = true;
        String productName = product.getName();
        webClient.get().uri("/pr/product/exist/" + productName)
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(bool1);

        ProductDetails productDetails = new ProductDetails();
        productDetails.setDescription("Normale Milch");
        productDetails.setCategory("milk");
        productDetails.setPrice(2.05);
        productDetails.setSize(1.5);
        EntityExchangeResult<ProductDetails> result2 =
                webClient.post().uri("/pr/product/details/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(productDetails)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(ProductDetails.class)
                        .returnResult();
        productDetails = result2.getResponseBody();


        Dates dates = new Dates();
        dates.setDate("20-08-2023");
        EntityExchangeResult<Dates> result3 =
                webClient.post().uri("/tr/dates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dates)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectBody(Dates.class)
                        .returnResult();
        dates = result3.getResponseBody();

        ProductBindProductDetails productBindInfos = new ProductBindProductDetails();
        productBindInfos.setProduct(product);
        productBindInfos.setProductDetails(productDetails);
        productBindInfos.setStartDate(dates);
        EntityExchangeResult<ProductBindProductDetails> result4 =
                webClient.post().uri("/pr/product/bind/infos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(productBindInfos)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(ProductBindProductDetails.class)
                        .returnResult();
        productBindInfos = result4.getResponseBody();

        List<ProductBindProductDetails> list1 = new ArrayList<>();
        list1.add(productBindInfos);
        webClient.get().uri("/pr/product/bind/infos/milk")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ProductBindProductDetails.class)
                .isEqualTo(list1);

        Tour tour = new Tour();
        tour.setNumber("1");
        EntityExchangeResult<Tour> result6 =
                webClient.post().uri("/tr/tour/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tour)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectBody(Tour.class)
                        .returnResult();

        tour = result6.getResponseBody();

        Ordered ordered = new Ordered();
        ordered.setDeliverPeople(resultUser.getResponseBody());
        ordered.setProductBindInfos(productBindInfos);
        ordered.setQuantityOrdered(2);
        ordered.setDate(dates);
        ordered.setTour(tour);
        EntityExchangeResult<Ordered> result5 =
                webClient.post().uri("/or/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(ordered)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(Ordered.class)
                        .returnResult();
        ordered = result5.getResponseBody();

        webClient.get().uri("/or/order/admin/milk/"+ productDetails.getId() + "/" + dates.getId() + '/' + tour.getNumber())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Ordered.class)
                .isEqualTo(ordered);

        webClient.get().uri("/or/order/exist/admin/milk/"+ productDetails.getId() + "/" + dates.getId() + '/' + tour.getNumber())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(bool1);

        List<Ordered> list2 = new ArrayList<>();
        list2.add(ordered);
        webClient.get().uri("/or/order/admin")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Ordered.class)
                .isEqualTo(list2);


        TourBindDatesAndProductBindInfo tourBindDatesAndProductBindInfo = new TourBindDatesAndProductBindInfo();
        tourBindDatesAndProductBindInfo.setTour(tour);
        tourBindDatesAndProductBindInfo.setDates(dates);
        tourBindDatesAndProductBindInfo.setProductBindInfos(productBindInfos);
        EntityExchangeResult<TourBindDatesAndProductBindInfo> result7 =
                webClient.post().uri("/tr/tour/bind/dates/product/infos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(tourBindDatesAndProductBindInfo)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(TourBindDatesAndProductBindInfo.class)
                        .returnResult();
                tourBindDatesAndProductBindInfo = result7.getResponseBody();
        webClient.get().uri("/tr/tour/bind/dates/product/infos/1/"+ dates.getId()+ "/milk/" + productDetails.getId())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TourBindDatesAndProductBindInfo.class)
                .isEqualTo(tourBindDatesAndProductBindInfo);

        webClient.get().uri("/tr/tour/bind/dates/product/infos/exist/1/"+ dates.getId()+"/milk/" + productDetails.getId())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(bool1);

        List<TourBindDatesAndProductBindInfo> list3 = new ArrayList<>();
        list3.add(tourBindDatesAndProductBindInfo);
        webClient.get().uri("/tr/tour/bind/dates/product/infos/1/"+ dates.getId())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TourBindDatesAndProductBindInfo.class)
                .isEqualTo(list3);

        webClient.get().uri("/tr/tour/bind/dates/product/infos/")
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TourBindDatesAndProductBindInfo.class)
                .isEqualTo(list3);

        webClient.delete().uri("/tr/tour/bind/dates/product/infos/1/"+ dates.getId()+"/milk/" + productDetails.getId())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk();

        webClient.get().uri("/tr/tour/bind/dates/product/infos/exist/1/"+ dates.getId()+"/milk/" + productDetails.getId())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);

        PersonProfileOrder personProfileOrder = new PersonProfileOrder();
        personProfileOrder.setUser(resultUser.getResponseBody());
        personProfileOrder.setProductBindProductDetails(productBindInfos);
        personProfileOrder.setTour(tour);
        EntityExchangeResult<PersonProfileOrder> result8 =
                webClient.post().uri("/or/order/person/profile/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(personProfileOrder)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(PersonProfileOrder.class)
                        .returnResult();
        personProfileOrder = result8.getResponseBody();

        webClient.get().uri("/or/order/person/profile/exist/" + resultUser.getResponseBody().getUsername() + '/' + productName + '/' + productDetails.getId() + '/' + tour.getNumber())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        List<PersonProfileOrder> list4 = new ArrayList<>();
        list4.add(personProfileOrder);
        webClient.get().uri("/or/order/person/profile/" + resultUser.getResponseBody().getUsername())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PersonProfileOrder.class)
                .isEqualTo(list4);

        webClient.delete().uri("/or/order/person/profile/" + resultUser.getResponseBody().getUsername() + '/' + productName + '/' + productDetails.getId() + '/' + tour.getNumber())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk();

        webClient.get().uri("/or/order/person/profile/exist/" + resultUser.getResponseBody().getUsername() + '/' + productName + '/' + productDetails.getId() + '/' + tour.getNumber())
                .headers(http -> http.setBearerAuth(finalToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);

    }


}
