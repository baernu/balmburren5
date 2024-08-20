package com.messerli.balmburren;



import com.fasterxml.jackson.databind.util.Annotations;
import com.messerli.balmburren.dtos.LoginUserDto;
import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import com.messerli.balmburren.responses.LoginResponse;
import com.messerli.balmburren.responses.StringResponse;
import com.messerli.balmburren.services.JwtService;
import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.*;

//@ActiveProfiles("test")

@Transactional
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BackendIntegrationTests_InvoiceTest {
    @Autowired
    private WebTestClient webClient;
    @Autowired
    private Optional<ProductDetails> productDetails;
    @Autowired
    private Optional<Dates> dates;
    @Autowired
    private Optional<ProductBindProductDetails> productBindInfos;
    @Autowired
    private Optional<Tour> tour;
    @Autowired
    private Optional<Ordered> ordered;

    @Autowired
    private RoleRepository roleRepository;




//    @BeforeEach
//    public void setup(){
//        webClient.post().uri("/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue("{\"password\": \"adminadmin\", \"username\": \"admin\" }")
//                .exchange()
//                .expectStatus().isOk();
//
//
//    }



    @Test
    public void TestProduct() {
        Product product = new Product();
        product.setName("milk");
        product.setId(1L);
        EntityExchangeResult<Product> result =
        webClient.post().uri("/pr/product/")
                .contentType(MediaType.APPLICATION_JSON)
//                        .headers(http -> http.setBearerAuth(token))
                .bodyValue(product)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Product.class)
//                .isEqualTo(product);
                .returnResult();
        Assertions.assertEquals("milk", result.getResponseBody().getName());

    }
    @Test
    public void TestUser() {


        RegisterUserDto userDto = new RegisterUserDto();

        userDto.setFirstname("Bernhard").setLastname("Messerli").setUsername("baernu").setPassword("123");

        EntityExchangeResult<User> result0 =
                webClient.post().uri("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDto)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(User.class)
                        .returnResult();

        User userRegistered = result0.getResponseBody();
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        if (optionalRole.isEmpty() || userRegistered == null) {
            return;
        }
        userRegistered.getRoles().add(optionalRole.get());

        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setUsername(userRegistered.getUsername()).setPassword("123");

        EntityExchangeResult<LoginResponse> loginResponse =
                webClient.post().uri("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(loginUserDto)
                        .exchange()
                        .expectStatus().isOk().expectBody(LoginResponse.class).returnResult();

        String token = loginResponse.getResponseBody().getToken();
        String finalToken = token;
        EntityExchangeResult<User> result1 =
                webClient.put().uri("/admins/update/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(userRegistered)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(User.class)
                        .returnResult();

        Assertions.assertEquals("Bernhard", result1.getResponseBody().getFirstname());

        EntityExchangeResult<List<User>> resultUsers =
                webClient.get().uri("/users/")
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(User.class)
                        .returnResult();

        Assertions.assertEquals("Bernhard", resultUsers.getResponseBody().get(1).getFirstname());

        EntityExchangeResult<User> resultUser =
                webClient.get().uri("/users/baernu")
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(User.class)
                        .returnResult();


        Assertions.assertEquals("Bernhard", resultUser.getResponseBody().getFirstname());


        EntityExchangeResult<List<Role>> resultRoles =
                webClient.get().uri("/users/role")
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(Role.class)
                        .returnResult();

        Assertions.assertEquals(RoleEnum.ADMIN, resultRoles.getResponseBody().get(1).getName());

        EntityExchangeResult<Boolean> existUser =
                webClient.get().uri("/auth/exist/baernu")
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(Boolean.class)
                        .returnResult();

        Assertions.assertEquals(true, existUser.getResponseBody());

    }

    @Test
    public void TestDeliver() {


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
        product.setId(1L);
        EntityExchangeResult<Product> result =
                webClient.post().uri("/pr/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(product)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(Product.class)
                        .returnResult();
        Assertions.assertEquals("milk", result.getResponseBody().getName());

        productDetails = Optional.of(new ProductDetails());
        productDetails.get().setDescription("Normale Milch");
        productDetails.get().setCategory("milk");
        productDetails.get().setPrice(2.05);
        productDetails.get().setSize(1.5);
        EntityExchangeResult<Optional<ProductDetails>> resultProductDetails =
                webClient.post().uri("/pr/product/details/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(http -> http.setBearerAuth(finalToken))
                        .bodyValue(productDetails)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<Optional<ProductDetails>>() {})
                        .returnResult();
        productDetails = resultProductDetails.getResponseBody();
        Assertions.assertTrue(productDetails.isPresent(), "ProductDetails should be present");
        Assertions.assertEquals("milk", resultProductDetails.getResponseBody().get().getCategory());


        dates = Optional.of(new Dates());
        dates.get().setDate("21-08-2023");
        EntityExchangeResult<Optional<Dates>> resultDates =
                webClient.post().uri("/tr/dates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dates)
                        .exchange()
                        .expectBody(new ParameterizedTypeReference<Optional<Dates>>() {})
                        .returnResult();
        dates = resultDates.getResponseBody();
        Assertions.assertTrue(dates.isPresent(), "Dates should be present");
        Assertions.assertEquals("21-08-2023", resultDates.getResponseBody().get().getDate());


        productBindInfos = Optional.of(new ProductBindProductDetails());
        productBindInfos.get().setProduct(product);
        productBindInfos.get().setProductDetails(productDetails.get());
        productBindInfos.get().setStartDate(dates.get());
        EntityExchangeResult<Optional<ProductBindProductDetails>> resultProductBindProductDetails =
                webClient.post().uri("/pr/product/bind/infos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productBindInfos)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<Optional<ProductBindProductDetails>>() {})
                        .returnResult();
        Optional<ProductBindProductDetails> productBindProductDetails = resultProductBindProductDetails.getResponseBody();
        Assertions.assertTrue(productBindInfos.isPresent(), "ProductBindInfos should be present");
        Assertions.assertEquals("milk", productBindProductDetails.get().getProduct().getName());
        productBindInfos = productBindProductDetails;

        tour = Optional.of(new Tour());
        tour.get().setNumber("1");
        EntityExchangeResult<Optional<Tour>> resultTour =
                webClient.post().uri("/tr/tour/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tour)
                        .exchange()
                        .expectBody(new ParameterizedTypeReference<Optional<Tour>>() {})
                        .returnResult();

        Optional<Tour> tour1 = resultTour.getResponseBody();
        Assertions.assertTrue(tour1.isPresent(), "Tour should be present");
        Assertions.assertEquals("1", tour1.get().getNumber());
        tour = tour1;



        ordered = Optional.of(new Ordered());
        ordered.get().setDeliverPeople(resultUser.getResponseBody());
        ordered.get().setProductBindInfos(productBindInfos.get());
        ordered.get().setQuantityOrdered(2);
        ordered.get().setDate(dates.get());
        ordered.get().setTour(tour.get());


        EntityExchangeResult<Optional<Ordered>> result5 =
                webClient.post().uri("/or/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ordered)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<Optional<Ordered>>() {})
                        .returnResult();
        ordered = result5.getResponseBody();
        Assertions.assertTrue(ordered.isPresent(), "Ordered should be present");

    }

    @Test
    void postInvoice() {

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

        dates = Optional.of(new Dates());
        dates.get().setDate("22-08-2023");
        EntityExchangeResult<Optional<Dates>> resultDates =
                webClient.post().uri("/tr/dates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dates)
                        .exchange()
                        .expectBody(new ParameterizedTypeReference<Optional<Dates>>() {})
                        .returnResult();
        dates = resultDates.getResponseBody();
        Assertions.assertTrue(dates.isPresent(), "Dates should be present");
        Assertions.assertEquals("22-08-2023", resultDates.getResponseBody().get().getDate());

        EntityExchangeResult<User> resultUser =
                webClient.get().uri("/users/admin")
                        .headers(http -> http.setBearerAuth(finalToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(User.class)
                        .returnResult();


        Assertions.assertEquals("Normal", resultUser.getResponseBody().getFirstname());
        Optional<User> userOptional = Optional.ofNullable(resultUser.getResponseBody());

        Optional<Invoice> invoice = Optional.of(new Invoice());
        invoice.get().setAmount(1000.02);
        invoice.get().setIsPaid(false);
        EntityExchangeResult<Optional<Invoice>> result1 =
                webClient.post().uri("/ic/invoice/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(invoice)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<Optional<Invoice>>() {})
                        .returnResult();
        invoice = result1.getResponseBody();
        Assertions.assertTrue(invoice.isPresent(), "Invoice should be present");

        Optional<PersonBindInvoice> personBindInvoice = Optional.of(new PersonBindInvoice());
        personBindInvoice.get().setInvoice(invoice.get());
        personBindInvoice.get().setPersonInvoice(userOptional.get());
        personBindInvoice.get().setPersonDeliver(userOptional.get());
        personBindInvoice.get().setDateFrom(dates.get());
        personBindInvoice.get().setDateTo(dates.get());
        EntityExchangeResult<Optional<PersonBindInvoice>> result2 =
                webClient.post().uri("/bd/person/bind/invoice/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(personBindInvoice)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<Optional<PersonBindInvoice>>() {})
                        .returnResult();
        personBindInvoice = result2.getResponseBody();
        Assertions.assertTrue(personBindInvoice.isPresent(), "PersonBindInvoice should be present");

        Optional<List<PersonBindInvoice>> list = Optional.of(new ArrayList<>());
        list.get().add(personBindInvoice.get());

        EntityExchangeResult<Optional<List<PersonBindInvoice>>> result3 =
                webClient.get().uri("/bd/person/bind/invoice/")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(new ParameterizedTypeReference<Optional<List<PersonBindInvoice>>>() {})
                        .returnResult();


        Optional<List<PersonBindInvoice>> list1 = result3.getResponseBody();
        Assertions.assertTrue(list1.isPresent(), "PersonBindInvoice should be present");
        Assertions.assertEquals(list.get(), list1.get(), "The list of PersonBindInvoice should match");

        EntityExchangeResult<Optional<List<PersonBindInvoice>>> pbi1=
        webClient.get().uri("/bd/person/bind/invoice/invoice/" + userOptional.get().getUsername())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Optional<List<PersonBindInvoice>>>() {})
                .returnResult();

        Assertions.assertEquals(list.get(), pbi1.getResponseBody().get());

        webClient.get().uri("/bd/person/bind/invoice/" + dates.get().getDate())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Optional<List<PersonBindInvoice>>>() {})
                .isEqualTo(list1);

        webClient.get().uri("/bd/person/bind/invoice/deliver/" + userOptional.get().getUsername())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Optional<List<PersonBindInvoice>>>() {})
                .isEqualTo(list1);

        webClient.get().uri("/bd/person/bind/invoice/exist/" + dates.get().getId() + '/' + dates.get().getId() + '/' +
                        userOptional.get().getUsername() + '/' + userOptional.get().getUsername())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        webClient.get().uri("/bd/person/bind/invoice/" + dates.get().getId() + '/' + dates.get().getId() + '/' +
                        userOptional.get().getUsername() + '/' + userOptional.get().getUsername())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Optional<PersonBindInvoice>>() {})
                .isEqualTo(personBindInvoice);

        webClient.delete().uri("/bd/person/bind/invoice/" + dates.get().getId() + '/' + dates.get().getId() + '/' +
                        userOptional.get().getUsername() + '/' + userOptional.get().getUsername())
                .exchange()
                .expectStatus()
                .isOk();

        webClient.get().uri("/bd/person/bind/invoice/exist/" + dates.get().getId() + '/' + dates.get().getId() + '/' +
                        userOptional.get().getUsername() + '/' + userOptional.get().getUsername())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);


    }

}
