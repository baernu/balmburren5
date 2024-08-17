package com.messerli.balmburren;



import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.*;

//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
@Transactional
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BackendIntegrationTests_InvoiceTest {
    @Autowired
    private WebTestClient webClient;
    @Autowired
    private Optional<User> people;
    @Autowired
    private Optional<Product> product;
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
    private RegisterUserDto userDto;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @BeforeEach
    public void setup() {

        userDto = new RegisterUserDto();
        userDto.setFirstname("Normal").setLastname("Admin").setUsername("admin").setPassword("adminadmin");
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        Optional<Role> optionalRole1 = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        Set<Role> roles = new HashSet<>();
        roles.add(optionalRole.get());
        roles.add(optionalRole1.get());
//
        var user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
//					user.setRoles(roles);

        User user1 = userRepository.save(user);
        user1.setRoles(roles);
        user1 = userRepository.save(user1);

        webClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8006/api/")
                .build();

        webClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"password\": \"adminadmin\", \"username\": \"admin\" }")
                .exchange()
                .expectStatus().isOk();

//        EntityExchangeResult<User> result =
//                webClient.get().uri("users/admin")
//                        .exchange()
//                        .expectBody(User.class)
//                        .returnResult();
//        User people1 = result.getResponseBody();
//        assert people1 != null;
//        Assertions.assertEquals(people1.getUsername(), "admin");
        EntityExchangeResult<Optional<User>> result =
                webClient.get().uri("/users/admin")
                        .exchange()
                        .expectBody(new ParameterizedTypeReference<Optional<User>>() {})
                        .returnResult();

        Optional<User> userOptional = result.getResponseBody();
        Assertions.assertTrue(userOptional.isPresent(), "User should be present");
        Assertions.assertEquals("admin", userOptional.get().getUsername());

//        this.token = this.people.getToken();


        product = Optional.of(new Product());
        product.get().setName("milk");
        EntityExchangeResult<Optional<Product>> result1 =
                webClient.post().uri("/pr/product/")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .headers(http -> http.setBearerAuth(token))
                        .bodyValue(product)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<Optional<Product>>() {})
                        .returnResult();
        product = result1.getResponseBody();
        Assertions.assertTrue(product.isPresent(), "Product should be present");

        productDetails = Optional.of(new ProductDetails());
        productDetails.get().setDescription("Normale Milch");
        productDetails.get().setCategory("milk");
        productDetails.get().setPrice(2.05);
        productDetails.get().setSize(1.5);
        EntityExchangeResult<Optional<ProductDetails>> result2 =
                webClient.post().uri("/pr/product/details/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productDetails)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<Optional<ProductDetails>>() {})
                        .returnResult();
        productDetails = result2.getResponseBody();
        Assertions.assertTrue(productDetails.isPresent(), "ProductDetails should be present");

        dates = Optional.of(new Dates());
        dates.get().setDate("20-08-2023");
        EntityExchangeResult<Optional<Dates>> result3 =
                webClient.post().uri("/tr/dates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dates)
                        .exchange()
                        .expectBody(new ParameterizedTypeReference<Optional<Dates>>() {})
                        .returnResult();
        dates = result3.getResponseBody();
        Assertions.assertTrue(dates.isPresent(), "Dates should be present");

        productBindInfos = Optional.of(new ProductBindProductDetails());
        productBindInfos.get().setProduct(product.get());
        productBindInfos.get().setProductDetails(productDetails.get());
        productBindInfos.get().setStartDate(dates.get());
        EntityExchangeResult<Optional<ProductBindProductDetails>> result4 =
                webClient.post().uri("/pr/product/bind/infos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productBindInfos)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<Optional<ProductBindProductDetails>>() {})
                        .returnResult();
        productBindInfos = result4.getResponseBody();
        Assertions.assertTrue(productBindInfos.isPresent(), "ProductBindInfos should be present");

        tour = Optional.of(new Tour());
        tour.get().setNumber("1");
        EntityExchangeResult<Optional<Tour>> result6 =
                webClient.post().uri("/tr/tour/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tour)
                        .exchange()
                        .expectBody(new ParameterizedTypeReference<Optional<Tour>>() {})
                        .returnResult();

        tour = result6.getResponseBody();
        Assertions.assertTrue(tour.isPresent(), "Tour should be present");

        ordered = Optional.of(new Ordered());
        ordered.get().setDeliverPeople(people.get());
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
//        Optional<Invoice> invoice = Optional.of(new Invoice());
//        invoice.get().setAmount(1000.02);
//        invoice.get().setIsPaid(false);
//        EntityExchangeResult<Optional<Invoice>> result1 =
//                webClient.post().uri("/ic/invoice/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(invoice)
//                        .exchange()
//                        .expectStatus()
//                        .isCreated()
//                        .expectBody(new ParameterizedTypeReference<Optional<Invoice>>() {})
//                        .returnResult();
//        invoice = result1.getResponseBody();
//        Assertions.assertTrue(invoice.isPresent(), "Invoice should be present");
//
//        Optional<PersonBindInvoice> personBindInvoice = Optional.of(new PersonBindInvoice());
//        personBindInvoice.get().setInvoice(invoice.get());
//        personBindInvoice.get().setPersonInvoice(people.get());
//        personBindInvoice.get().setPersonDeliver(people.get());
//        personBindInvoice.get().setDateFrom(dates.get());
//        personBindInvoice.get().setDateTo(dates.get());
//        EntityExchangeResult<Optional<PersonBindInvoice>> result2 =
//                webClient.post().uri("bd/person/bind/invoice/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(personBindInvoice)
//                        .exchange()
//                        .expectStatus()
//                        .isCreated()
//                        .expectBody(new ParameterizedTypeReference<Optional<PersonBindInvoice>>() {})
//                        .returnResult();
//        personBindInvoice = result2.getResponseBody();
//        Assertions.assertTrue(personBindInvoice.isPresent(), "PersonBindInvoice should be present");

//        List<PersonBindInvoice> list = new ArrayList<>();
//        list.add(personBindInvoice);
//        webClient.get().uri("bd/person/bind/invoice/")
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(PersonBindInvoice.class)
//                .isEqualTo(list);
//        webClient.get().uri("bd/person/bind/invoice/invoice/" + people.get().getUsername())
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(PersonBindInvoice.class)
//                .isEqualTo(list);
//        webClient.get().uri("bd/person/bind/invoice/" + dates.getDate())
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(PersonBindInvoice.class)
//                .isEqualTo(list);
//        webClient.get().uri("bd/person/bind/invoice/deliver/" + people.getUsername())
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(PersonBindInvoice.class)
//                .isEqualTo(list);
//        webClient.get().uri("bd/person/bind/invoice/exist/" + dates.getId() + '/' + dates.getId() + '/' +
//                        people.getUsername() + '/' + people.getUsername())
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(Boolean.class)
//                .isEqualTo(true);
//        webClient.get().uri("bd/person/bind/invoice/" + dates.getId() + '/' + dates.getId() + '/' +
//                        people.getUsername() + '/' + people.getUsername())
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(PersonBindInvoice.class)
//                .isEqualTo(personBindInvoice);
//        webClient.delete().uri("bd/person/bind/invoice/" + dates.getId() + '/' + dates.getId() + '/' +
//                        people.getUsername() + '/' + people.getUsername())
//                .exchange()
//                .expectStatus()
//                .isOk();
//        webClient.get().uri("bd/person/bind/invoice/exist/" + dates.getId() + '/' + dates.getId() + '/' +
//                        people.getUsername() + '/' + people.getUsername())
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(Boolean.class)
//                .isEqualTo(false);


    }

}
