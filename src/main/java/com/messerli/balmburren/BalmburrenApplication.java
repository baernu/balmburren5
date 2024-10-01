package com.messerli.balmburren;

import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.*;

import com.messerli.balmburren.repositories.ProductRepo;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import com.messerli.balmburren.repositories.UsersRoleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.*;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
//@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
public class BalmburrenApplication  {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(BalmburrenApplication.class, args);
	}


	@Bean
	CommandLineRunner init(RoleRepository roleRepository,UsersRoleRepo usersRoleRepo,UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {


			loadRoles();

			RegisterUserDto userDto = new RegisterUserDto();
			userDto.setFirstname("Normal").setLastname("Admin").setUsername("admin").setPassword("adminadmin");

			var user = new User();
			user.setFirstname(userDto.getFirstname());
			user.setLastname(userDto.getLastname());
			user.setUsername(userDto.getUsername());
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
			user.setEnabled(true);
			if(userRepository.existsByUsername(userDto.getUsername()))
				return;
			User user1 = userRepository.save(user);


			Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
			Optional<Role> optionalRole1 = roleRepository.findByName(RoleEnum.USER);
			if (optionalRole.isEmpty() || optionalRole1.isEmpty() || user1.getUsername().isEmpty()) {
				return;
			}

			Optional<UsersRole> usersRole = Optional.of(new UsersRole());
			usersRole.get().setUser(user1);
			usersRole.get().setRole(optionalRole.get());
			usersRoleRepo.save(usersRole.get());
			userRepository.save(user1);
		};
	}

	private void loadRoles() {
		RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN, RoleEnum.DRIVER, RoleEnum.KATHY, RoleEnum.USER_KATHY };
		Map<RoleEnum, String> roleDescriptionMap = Map.of(
				RoleEnum.USER, "Default user role",
				RoleEnum.DRIVER, "Driver role",
				RoleEnum.KATHY, "Helper role",
				RoleEnum.USER_KATHY, "User administrated by Kathy",
				RoleEnum.ADMIN, "Administrator role",
				RoleEnum.SUPER_ADMIN, "Super Administrator role"
		);

		Arrays.stream(roleNames).forEach((roleName) -> {
			Optional<Role> optionalRole = roleRepository.findByName(roleName);

			optionalRole.ifPresentOrElse(System.out::println, () -> {
				Role roleToCreate = new Role();

				roleToCreate.setName(roleName);
				roleToCreate.setDescription(roleDescriptionMap.get(roleName));

				roleRepository.save(roleToCreate);
			});
		});


	}
}


