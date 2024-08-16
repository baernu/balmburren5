package com.messerli.balmburren;



import com.messerli.balmburren.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.ArrayList;
import java.util.List;

//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BackendIntegrationTests_InvoiceTest {

    private WebTestClient webClient;
    private String token;
    private User people;
    private Product product;
    private ProductDetails productDetails;
    private Dates dates;
    private ProductBindProductDetails productBindInfos;
    private Tour tour;
    private Ordered ordered;


    @BeforeEach
    public void setup() {
        webClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8006/api/")
                .build();

        webClient.post().uri("auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"password\": \"adminadmin\", \"username\": \"admin\" }")
                .exchange()
                .expectStatus().isOk();

        EntityExchangeResult<User> result =
                webClient.get().uri("users/admin")
                        .exchange()
                        .expectBody(User.class)
                        .returnResult();
        this.people = result.getResponseBody();


//        this.token = this.people.getToken();


        product = new Product();
        product.setName("milk");
        EntityExchangeResult<Product> result1 =
                webClient.post().uri("pr/product/")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .headers(http -> http.setBearerAuth(token))
                        .bodyValue(product)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(Product.class)
                        .returnResult();
        product = result1.getResponseBody();

        productDetails = new ProductDetails();
        productDetails.setDescription("Normale Milch");
        productDetails.setCategory("milk");
        productDetails.setPrice(2.05);
        productDetails.setSize(1.5);
        EntityExchangeResult<ProductDetails> result2 =
                webClient.post().uri("pr/product/details/")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .headers(http -> http.setBearerAuth(token))
                        .bodyValue(productDetails)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(ProductDetails.class)
                        .returnResult();
        productDetails = result2.getResponseBody();


        dates = new Dates();
        dates.setDate("20-08-2023");
        EntityExchangeResult<Dates> result3 =
                webClient.post().uri("tr/dates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dates)
//                        .headers(http -> http.setBearerAuth(token))
                        .exchange()
                        .expectBody(Dates.class)
                        .returnResult();
        dates = result3.getResponseBody();

        productBindInfos = new ProductBindProductDetails();
        productBindInfos.setProduct(product);
        productBindInfos.setProductDetails(productDetails);
        productBindInfos.setStartDate(dates);
        EntityExchangeResult<ProductBindProductDetails> result4 =
                webClient.post().uri("pr/product/bind/infos/")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .headers(http -> http.setBearerAuth(token))
                        .bodyValue(productBindInfos)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(ProductBindProductDetails.class)
                        .returnResult();
        productBindInfos = result4.getResponseBody();

        tour = new Tour();
        tour.setNumber("1");
        EntityExchangeResult<Tour> result6 =
                webClient.post().uri("tr/tour/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tour)
//                        .headers(http -> http.setBearerAuth(token))
                        .exchange()
                        .expectBody(Tour.class)
                        .returnResult();

        tour = result6.getResponseBody();

        ordered = new Ordered();
        ordered.setDeliverPeople(people);
        ordered.setProductBindInfos(productBindInfos);
        ordered.setQuantityOrdered(2);
        ordered.setDate(dates);
        ordered.setTour(tour);
        EntityExchangeResult<Ordered> result5 =
                webClient.post().uri("or/order/")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .headers(http -> http.setBearerAuth(token))
                        .bodyValue(ordered)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(Ordered.class)
                        .returnResult();
        ordered = result5.getResponseBody();
    }

    @Test
    void postInvoice() {
//        Invoice invoice = new Invoice();
//        invoice.setAmount(1000.02);
//        invoice.setIsPaid(false);
//        EntityExchangeResult<Invoice> result1 =
//                webClient.post().uri("ic/invoice/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .headers(http -> http.setBearerAuth(token))
//                        .bodyValue(invoice)
//                        .exchange()
//                        .expectStatus()
//                        .isCreated()
//                        .expectBody(Invoice.class)
//                        .returnResult();
//        invoice = result1.getResponseBody();
//
//        PersonBindInvoice personBindInvoice = new PersonBindInvoice();
//        personBindInvoice.setInvoice(invoice);
//        personBindInvoice.setPersonInvoice(people);
//        personBindInvoice.setPersonDeliver(people);
//        personBindInvoice.setDateFrom(dates);
//        personBindInvoice.setDateTo(dates);
//        EntityExchangeResult<PersonBindInvoice> result2 =
//                webClient.post().uri("bd/person/bind/invoice/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .headers(http -> http.setBearerAuth(token))
//                        .bodyValue(personBindInvoice)
//                        .exchange()
//                        .expectStatus()
//                        .isCreated()
//                        .expectBody(PersonBindInvoice.class)
//                        .returnResult();
//        personBindInvoice = result2.getResponseBody();
//
//        List<PersonBindInvoice> list = new ArrayList<>();
//        list.add(personBindInvoice);
//        webClient.get().uri("bd/person/bind/invoice/")
//                .headers(http -> http.setBearerAuth(token))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(PersonBindInvoice.class)
//                .isEqualTo(list);
//        webClient.get().uri("bd/person/bind/invoice/invoice/" + people.getUsername())
//                .headers(http -> http.setBearerAuth(token))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(PersonBindInvoice.class)
//                .isEqualTo(list);
//        webClient.get().uri("bd/person/bind/invoice/" + dates.getDate())
//                .headers(http -> http.setBearerAuth(token))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(PersonBindInvoice.class)
//                .isEqualTo(list);
//        webClient.get().uri("bd/person/bind/invoice/deliver/" + people.getUsername())
//                .headers(http -> http.setBearerAuth(token))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(PersonBindInvoice.class)
//                .isEqualTo(list);
//        webClient.get().uri("bd/person/bind/invoice/exist/" + dates.getId() + '/' + dates.getId() + '/' +
//                        people.getUsername() + '/' + people.getUsername())
//                .headers(http -> http.setBearerAuth(token))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(Boolean.class)
//                .isEqualTo(true);
//        webClient.get().uri("bd/person/bind/invoice/" + dates.getId() + '/' + dates.getId() + '/' +
//                        people.getUsername() + '/' + people.getUsername())
//                .headers(http -> http.setBearerAuth(token))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(PersonBindInvoice.class)
//                .isEqualTo(personBindInvoice);
//        webClient.delete().uri("bd/person/bind/invoice/" + dates.getId() + '/' + dates.getId() + '/' +
//                        people.getUsername() + '/' + people.getUsername())
//                .headers(http -> http.setBearerAuth(token))
//                .exchange()
//                .expectStatus()
//                .isOk();
//        webClient.get().uri("bd/person/bind/invoice/exist/" + dates.getId() + '/' + dates.getId() + '/' +
//                        people.getUsername() + '/' + people.getUsername())
//                .headers(http -> http.setBearerAuth(token))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(Boolean.class)
//                .isEqualTo(false);


    }

}
