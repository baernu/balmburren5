package com.messerli.balmburren;

import com.messerli.balmburren.dtos.LoginUserDto;
import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.services.AuthenticationService;
import com.messerli.balmburren.services.DatesService;
import com.messerli.balmburren.services.TourService;
import com.messerli.balmburren.services.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
@Transactional
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private TourService tourService;
    @Autowired
    private DatesService datesService;
//    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;


    @Before
    public void setUp()
    {
        registerUserDto = new RegisterUserDto();
        registerUserDto.setFirstname("Bernhard");
        registerUserDto.setLastname("Messerli");
        registerUserDto.setPassword("123");
        registerUserDto.setUsername("baernu2");
        authenticationService.signup(registerUserDto);
        loginUserDto = new LoginUserDto();
        loginUserDto.setPassword("adminadmin").setUsername("admin");
//        loginUserDto.setUsername("admin");
        authenticationService.authenticate(loginUserDto);

    }
    @Test
    public void persistUser() {
        
        
//        authenticationService.authenticate(loginUserDto);
//        userService.createAdministrator("baernu");

//        Role superAdmin = userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
//        User people = new User(null,0, "Bernhard", "Messerli", "@baernu", "123", null);
//        userService.saveUser(people);
//        People people1 = userService.getUser(people.getUsername());
//        PersonBindRole personBindRole = new PersonBindRole(null, 0, people, superAdmin);
//        userService.savePersonBindRole(personBindRole);
//        assertEquals(userService.getPersonBindRole(people, superAdmin).getRole(), superAdmin);
//        assertEquals(userService.(people).get(0).getRole(), superAdmin);
//        assertEquals(people, people1);



//        Optional<Tour> tour = Optional.of(new Tour());
//        tour.get().setNumber("1");
//        tour = tourService.saveTour(tour.get());
//        Optional<Dates> dates1 = Optional.of(new Dates());
//            Optional<Dates> dates2 = Optional.of(new Dates());
//        dates2.get().setDate("2023-05-09T00:00:00.000Z");
//        dates1.get().setDate(new Date().toString());
//        dates1 = datesService.saveDate(dates1.get());
//        dates2 = datesService.saveDate(dates2.get());
//        Optional<TourBindDates> tourBindDates = Optional.of(new TourBindDates());
//        Optional<TourBindDates> tourBindDates1 = Optional.of(new TourBindDates());
//        tourBindDates.get().setDates(dates1.get());
//        tourBindDates.get().setTour(tour.get());
//        tourBindDates1.get().setDates(dates2.get());
//        tourBindDates1.get().setTour(tour.get());
//        tourBindDates = tourService.saveTourBindDates(tourBindDates.get());
//        tourBindDates1 = tourService.saveTourBindDates(tourBindDates1.get());
////        assertEquals(tourService.getAllTourBindDatesForTour(tour).get(0), tourBindDates);
//        assertEquals(tourService.getTourBindDates(tour.get(), dates1.get()), tourBindDates);
//        tourService.deleteTourBindDates(tour.get(), dates1.get());
//        assertEquals(tourService.getAllTourBindDatesForTour(tour.get()).isPresent(), true);
//        assertEquals(tourService.getTourBindDates(tour.get(), dates2.get()), tourBindDates1);
    }

}
