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
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class BalmburrenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalmburrenApplication.class, args);
	}


	@Bean
	CommandLineRunner init(RoleRepository roleRepository,ReferenceRepo referenceRepo ,UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {



			RegisterUserDto userDto = new RegisterUserDto();
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
			log.info("Created User is : " + user1);

		};
	}
}


