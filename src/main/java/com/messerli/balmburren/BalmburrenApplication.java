package com.messerli.balmburren;

import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.Reference;
import com.messerli.balmburren.entities.Role;
import com.messerli.balmburren.entities.RoleEnum;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.repositories.ReferenceRepo;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
public class BalmburrenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalmburrenApplication.class, args);
	}


	@Bean
	CommandLineRunner init(RoleRepository roleRepository,ReferenceRepo referenceRepo ,UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			Reference reference = new Reference();
            reference.setName("invoiceReference");
			referenceRepo.save(reference);


			RegisterUserDto userDto = new RegisterUserDto();
//        userDto.setFirstname("Super").setLastname( "Admin").setUsername("super.admin@email.com").setPassword("123456");
			userDto.setFirstname("Normal").setLastname("Admin").setUsername("admin").setPassword("adminadmin");
//        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
//        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
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
//        User registeredUser = authenticationService.signup(userDto);
//        log.info("AuthenticationService is over..");
//        boolean message = userService.createAdministrator(registeredUser.getUsername());
			log.info("Created User is : " + user1);

		};
//		return null;
	}
}


