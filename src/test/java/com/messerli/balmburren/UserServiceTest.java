package com.messerli.balmburren;

import com.messerli.balmburren.dtos.LoginUserDto;
import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import com.messerli.balmburren.repositories.UsersRoleRepo;
import com.messerli.balmburren.services.AuthenticationService;
import com.messerli.balmburren.services.DatesService;
import com.messerli.balmburren.services.TourService;
import com.messerli.balmburren.services.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

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
    private AuthenticationService authenticationService;
    private RegisterUserDto userDto;
    private LoginUserDto loginUserDto;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UsersRoleRepo usersRoleRepo;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Before
    public void setUp()
    {

        userDto = new RegisterUserDto();
        userDto.setFirstname("Normal").setLastname("Admin").setUsername("admin").setPassword("adminadmin");
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        Optional<Role> optionalRole1 = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }
        var user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
//					user.setRoles(roles);

        User user1 = userRepository.save(user);

        UsersRole usersRole = new UsersRole();
        usersRole.setUser(user1);
        usersRole.setRole(optionalRole.get());
        usersRole = usersRoleRepo.save(usersRole);

        UsersRole usersRole1 = new UsersRole();
        usersRole1.setUser(user1);
        usersRole1.setRole(optionalRole1.get());
        usersRole1 = usersRoleRepo.save(usersRole1);


//


//        List<UsersRole> roles = usersRoleRepo.findAllByUser(user1);
//        roles.add(usersRole);
//        roles.add(usersRole1);
//        user1.setRoles(roles);
//        user1 = userRepository.save(user1);
    }
    @Test
    public void checkUserRole() {

        Optional<User> user = userService.findUser("admin");
        List<UsersRole> list = usersRoleRepo.findAllByUser(user.get());
        Optional<UsersRole> usersRole = list.stream().filter(e -> e.getRole().getName().equals(RoleEnum.ADMIN)).findFirst();

        assertEquals(roleRepository.findByName(RoleEnum.ADMIN), usersRole.get().getRole());

    }
    @Test
    public void checkTour() {

        Optional<Tour> tour = Optional.of(new Tour());
        tour.get().setNumber("1");
        tour = tourService.saveTour(tour.get());
        Optional<Dates> dates1 = Optional.of(new Dates());
        Optional<Dates> dates2 = Optional.of(new Dates());
        dates2.get().setDate("2023-05-09T00:00:00.000Z");
        dates1.get().setDate(new Date().toString());
        dates1 = datesService.saveDate(dates1.get());
        dates2 = datesService.saveDate(dates2.get());
        Optional<TourBindDates> tourBindDates = Optional.of(new TourBindDates());
        Optional<TourBindDates> tourBindDates1 = Optional.of(new TourBindDates());
        tourBindDates.get().setDates(dates1.get());
        tourBindDates.get().setTour(tour.get());
        tourBindDates1.get().setDates(dates2.get());
        tourBindDates1.get().setTour(tour.get());
        tourBindDates = tourService.saveTourBindDates(tourBindDates.get());
        tourBindDates1 = tourService.saveTourBindDates(tourBindDates1.get());
        assertEquals(tourService.getTourBindDates(tour.get(), dates1.get()), tourBindDates);
        tourService.deleteTourBindDates(tour.get(), dates1.get());
        assertEquals(tourService.getAllTourBindDatesForTour(tour.get()).isPresent(), true);
        assertEquals(tourService.getTourBindDates(tour.get(), dates2.get()), tourBindDates1);
    }

}
