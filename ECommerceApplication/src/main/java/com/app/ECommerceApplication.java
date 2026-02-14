package com.app;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.app.config.AppConstants;
import com.app.entites.Address;
import com.app.entites.Category;
import com.app.entites.Role;
import com.app.repositories.AddressRepo;
import com.app.repositories.CategoryRepo;
import com.app.repositories.RoleRepo;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@SecurityScheme(name = "E-Commerce Application", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class ECommerceApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private AddressRepo addressRepo;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role adminRole = new Role();
			adminRole.setRoleId(AppConstants.ADMIN_ID);
			adminRole.setRoleName("ADMIN");

			Role userRole = new Role();
			userRole.setRoleId(AppConstants.USER_ID);
			userRole.setRoleName("USER");

			List<Role> roles = List.of(adminRole, userRole);
			roleRepo.saveAll(roles);

			if (categoryRepo.count() == 0) {
				Category shoes = new Category();
				shoes.setCategoryName("Shoes");

				Category socks = new Category();
				socks.setCategoryName("Socks");

				categoryRepo.saveAll(List.of(shoes, socks));
			}

			if (addressRepo.count() == 0) {
				Address address1 = new Address();
				address1.setCountry("Indonesia");
				address1.setState("Jawa Barat");
				address1.setCity("Bandung");
				address1.setPincode("401111");
				address1.setStreet("Main Street 1");
				address1.setBuildingName("Block A");

				Address address2 = new Address();
				address2.setCountry("Indonesia");
				address2.setState("DKI Jakarta");
				address2.setCity("Jakarta");
				address2.setPincode("102200");
				address2.setStreet("Second Street 5");
				address2.setBuildingName("Tower B");

				addressRepo.saveAll(List.of(address1, address2));
			}

			System.out.println("Seed data initialized");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
