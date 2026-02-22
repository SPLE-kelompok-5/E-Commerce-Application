package com.app;

import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.app.config.AppConstants;
import com.app.entites.Address;
import com.app.entites.Cart;
import com.app.entites.Category;
import com.app.entites.OrderItem;
import com.app.entites.Product;
import com.app.entites.Role;
import com.app.entites.User;
import com.app.repositories.AddressRepo;
import com.app.repositories.CategoryRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.RoleRepo;
import com.app.repositories.UserRepo;

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

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private OrderItemRepo orderItemRepo;

  @Autowired
  private ProductRepo productRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PlatformTransactionManager transactionManager;

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
      TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

      transactionTemplate.execute(status -> {
        // ADMIN and USER roles
        if (roleRepo.count() == 0) {

          Role adminRole = new Role();

          adminRole.setRoleId(AppConstants.ADMIN_ID);

          adminRole.setRoleName("ADMIN");

          Role userRole = new Role();

          userRole.setRoleId(AppConstants.USER_ID);

          userRole.setRoleName("USER");

          List<Role> roles = List.of(adminRole, userRole);

          roleRepo.saveAll(roles);

        }

        // 10 Categories
        if (categoryRepo.count() == 0) {
          Category shoes = new Category();
          shoes.setCategoryName("Shoes");
          Category socks = new Category();
          socks.setCategoryName("Socks");
          Category shirts = new Category();
          shirts.setCategoryName("Shirts");
          Category pants = new Category();
          pants.setCategoryName("Pants");
          Category hats = new Category();
          hats.setCategoryName("Hats");
          Category gloves = new Category();
          gloves.setCategoryName("Gloves");
          Category watches = new Category();
          watches.setCategoryName("Watches");
          Category belts = new Category();
          belts.setCategoryName("Belts");
          Category bags = new Category();
          bags.setCategoryName("Bags");
          Category wallets = new Category();
          wallets.setCategoryName("Wallets");

          categoryRepo.saveAll(List.of(shoes, socks, shirts, pants, hats, gloves, watches, belts, bags, wallets));
        }

        // 10 Addresses
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

          Address address3 = new Address();
          address3.setCountry("Indonesia");
          address3.setState("Jawa Tengah");
          address3.setCity("Semarang");
          address3.setPincode("501111");
          address3.setStreet("Third Street 3");
          address3.setBuildingName("Building C");

          Address address4 = new Address();
          address4.setCountry("Indonesia");
          address4.setState("Jawa Timur");
          address4.setCity("Surabaya");
          address4.setPincode("601111");
          address4.setStreet("Fourth Street 4");
          address4.setBuildingName("Building D");

          Address address5 = new Address();
          address5.setCountry("Indonesia");
          address5.setState("Bali");
          address5.setCity("Denpasar");
          address5.setPincode("801111");
          address5.setStreet("Fifth Street 5");
          address5.setBuildingName("Building E");

          Address address6 = new Address();
          address6.setCountry("Indonesia");
          address6.setState("Banten");
          address6.setCity("Serang");
          address6.setPincode("421111");
          address6.setStreet("Sixth Street 6");
          address6.setBuildingName("Building F");

          Address address7 = new Address();
          address7.setCountry("Indonesia");
          address7.setState("DI Yogyakarta");
          address7.setCity("Yogyakarta");
          address7.setPincode("551111");
          address7.setStreet("Seventh Street 7");
          address7.setBuildingName("Building G");

          Address address8 = new Address();
          address8.setCountry("Indonesia");
          address8.setState("Sumatera Utara");
          address8.setCity("Medan");
          address8.setPincode("201111");
          address8.setStreet("Eighth Street 8");
          address8.setBuildingName("Building H");

          Address address9 = new Address();
          address9.setCountry("Indonesia");
          address9.setState("Sulawesi Selatan");
          address9.setCity("Makassar");
          address9.setPincode("901111");
          address9.setStreet("Ninth Street 9");
          address9.setBuildingName("Building I");

          Address address10 = new Address();
          address10.setCountry("Indonesia");
          address10.setState("Kalimantan Timur");
          address10.setCity("Samarinda");
          address10.setPincode("751111");
          address10.setStreet("Tenth Street 10");
          address10.setBuildingName("Building J");

          addressRepo.saveAll(List.of(address1, address2, address3, address4, address5, address6, address7, address8,
              address9, address10));
        }

        // 10 Users
        if (userRepo.count() == 0) {
          Role adminRole = roleRepo.findById(AppConstants.ADMIN_ID).orElseThrow();
          Role userRole = roleRepo.findById(AppConstants.USER_ID).orElseThrow();
          List<Address> addresses = addressRepo.findAll();

          User admin = new User();
          admin.setFirstName("Admin");
          admin.setLastName("User");
          admin.setEmail("admin@example.com");
          admin.setMobileNumber("1234567890");
          admin.setPassword(passwordEncoder.encode("admin123"));
          admin.setRoles(Set.of(adminRole));
          admin.setAddresses(List.of(addresses.get(0)));
          Cart adminCart = new Cart();
          adminCart.setUser(admin);
          admin.setCart(adminCart);

          User user1 = new User();
          user1.setFirstName("Johnny");
          user1.setLastName("Doenat");
          user1.setEmail("john.doe@example.com");
          user1.setMobileNumber("9876543210");
          user1.setPassword(passwordEncoder.encode("user123"));
          user1.setRoles(Set.of(userRole));
          user1.setAddresses(List.of(addresses.get(1)));
          Cart user1Cart = new Cart();
          user1Cart.setUser(user1);
          user1.setCart(user1Cart);

          User user2 = new User();
          user2.setFirstName("Janette");
          user2.setLastName("Smith");
          user2.setEmail("jane.smith@example.com");
          user2.setMobileNumber("5555555555");
          user2.setPassword(passwordEncoder.encode("user123"));
          user2.setRoles(Set.of(userRole));
          user2.setAddresses(List.of(addresses.get(2)));
          Cart user2Cart = new Cart();
          user2Cart.setUser(user2);
          user2.setCart(user2Cart);

          User user3 = new User();
          user3.setFirstName("Robert");
          user3.setLastName("Brown");
          user3.setEmail("robert.brown@example.com");
          user3.setMobileNumber("5555555556");
          user3.setPassword(passwordEncoder.encode("user123"));
          user3.setRoles(Set.of(userRole));
          user3.setAddresses(List.of(addresses.get(3)));
          Cart user3Cart = new Cart();
          user3Cart.setUser(user3);
          user3.setCart(user3Cart);

          User user4 = new User();
          user4.setFirstName("Emily");
          user4.setLastName("Davis");
          user4.setEmail("emily.davis@example.com");
          user4.setMobileNumber("5555555557");
          user4.setPassword(passwordEncoder.encode("user123"));
          user4.setRoles(Set.of(userRole));
          user4.setAddresses(List.of(addresses.get(4)));
          Cart user4Cart = new Cart();
          user4Cart.setUser(user4);
          user4.setCart(user4Cart);

          User user5 = new User();
          user5.setFirstName("Michael");
          user5.setLastName("Wilson");
          user5.setEmail("michael.wilson@example.com");
          user5.setMobileNumber("5555555558");
          user5.setPassword(passwordEncoder.encode("user123"));
          user5.setRoles(Set.of(userRole));
          user5.setAddresses(List.of(addresses.get(5)));
          Cart user5Cart = new Cart();
          user5Cart.setUser(user5);
          user5.setCart(user5Cart);

          User user6 = new User();
          user6.setFirstName("Sarah");
          user6.setLastName("Miller");
          user6.setEmail("sarah.miller@example.com");
          user6.setMobileNumber("5555555559");
          user6.setPassword(passwordEncoder.encode("user123"));
          user6.setRoles(Set.of(userRole));
          user6.setAddresses(List.of(addresses.get(6)));
          Cart user6Cart = new Cart();
          user6Cart.setUser(user6);
          user6.setCart(user6Cart);

          User user7 = new User();
          user7.setFirstName("David");
          user7.setLastName("Taylor");
          user7.setEmail("david.taylor@example.com");
          user7.setMobileNumber("5555555560");
          user7.setPassword(passwordEncoder.encode("user123"));
          user7.setRoles(Set.of(userRole));
          user7.setAddresses(List.of(addresses.get(7)));
          Cart user7Cart = new Cart();
          user7Cart.setUser(user7);
          user7.setCart(user7Cart);

          User user8 = new User();
          user8.setFirstName("Linda");
          user8.setLastName("Anderson");
          user8.setEmail("linda.anderson@example.com");
          user8.setMobileNumber("5555555561");
          user8.setPassword(passwordEncoder.encode("user123"));
          user8.setRoles(Set.of(userRole));
          user8.setAddresses(List.of(addresses.get(8)));
          Cart user8Cart = new Cart();
          user8Cart.setUser(user8);
          user8.setCart(user8Cart);

          User user9 = new User();
          user9.setFirstName("Thomas");
          user9.setLastName("Moore");
          user9.setEmail("thomas.moore@example.com");
          user9.setMobileNumber("5555555562");
          user9.setPassword(passwordEncoder.encode("user123"));
          user9.setRoles(Set.of(userRole));
          user9.setAddresses(List.of(addresses.get(9)));
          Cart user9Cart = new Cart();
          user9Cart.setUser(user9);
          user9.setCart(user9Cart);

          userRepo.saveAll(List.of(admin, user1, user2, user3, user4, user5, user6, user7, user8, user9));
        }

        // 10 Products
        if (productRepo.count() == 0) {
          List<Category> cats = categoryRepo.findAll();

          Product product1 = new Product();
          product1.setProductName("Nike Air Max");
          product1.setDescription("Premium running shoes");
          product1.setPrice(1500000);
          product1.setDiscount(10);
          product1.setSpecialPrice(1350000);
          product1.setQuantity(50);
          product1.setCategory(cats.get(0));

          Product product2 = new Product();
          product2.setProductName("Cotton Socks");
          product2.setDescription("Comfortable cotton socks");
          product2.setPrice(50000);
          product2.setDiscount(5);
          product2.setSpecialPrice(47500);
          product2.setQuantity(100);
          product2.setCategory(cats.get(1));

          Product product3 = new Product();
          product3.setProductName("White Shirt");
          product3.setDescription("Formal white shirt");
          product3.setPrice(300000);
          product3.setDiscount(0);
          product3.setSpecialPrice(300000);
          product3.setQuantity(40);
          product3.setCategory(cats.get(2));

          Product product4 = new Product();
          product4.setProductName("Blue Jeans");
          product4.setDescription("Classic blue jeans");
          product4.setPrice(450000);
          product4.setDiscount(10);
          product4.setSpecialPrice(405000);
          product4.setQuantity(25);
          product4.setCategory(cats.get(3));

          Product product5 = new Product();
          product5.setProductName("Baseball Hat");
          product5.setDescription("Cool baseball hat");
          product5.setPrice(150000);
          product5.setDiscount(0);
          product5.setSpecialPrice(150000);
          product5.setQuantity(60);
          product5.setCategory(cats.get(4));

          Product product6 = new Product();
          product6.setProductName("Leather Gloves");
          product6.setDescription("Warm leather gloves");
          product6.setPrice(250000);
          product6.setDiscount(15);
          product6.setSpecialPrice(212500);
          product6.setQuantity(20);
          product6.setCategory(cats.get(5));

          Product product7 = new Product();
          product7.setProductName("Digital Watch");
          product7.setDescription("Modern digital watch");
          product7.setPrice(500000);
          product7.setDiscount(5);
          product7.setSpecialPrice(475000);
          product7.setQuantity(15);
          product7.setCategory(cats.get(6));

          Product product8 = new Product();
          product8.setProductName("Leather Belt");
          product8.setDescription("Black leather belt");
          product8.setPrice(200000);
          product8.setDiscount(10);
          product8.setSpecialPrice(180000);
          product8.setQuantity(35);
          product8.setCategory(cats.get(7));

          Product product9 = new Product();
          product9.setProductName("Backpack");
          product9.setDescription("Large travel backpack");
          product9.setPrice(700000);
          product9.setDiscount(20);
          product9.setSpecialPrice(560000);
          product9.setQuantity(10);
          product9.setCategory(cats.get(8));

          Product product10 = new Product();
          product10.setProductName("Leather Wallet");
          product10.setDescription("Slim leather wallet");
          product10.setPrice(350000);
          product10.setDiscount(10);
          product10.setSpecialPrice(315000);
          product10.setQuantity(30);
          product10.setCategory(cats.get(9));

          productRepo.saveAll(List.of(product1, product2, product3, product4, product5, product6, product7, product8,
              product9, product10));
        }

        // 10 Order Items
        if (orderItemRepo.count() == 0) {
          List<Product> products = productRepo.findAll();

          OrderItem item1 = new OrderItem();
          item1.setProduct(products.get(0));
          item1.setQuantity(1);
          item1.setOrderedProductPrice(products.get(0).getSpecialPrice());

          OrderItem item2 = new OrderItem();
          item2.setProduct(products.get(1));
          item2.setQuantity(2);
          item2.setOrderedProductPrice(products.get(1).getSpecialPrice());

          OrderItem item3 = new OrderItem();
          item3.setProduct(products.get(2));
          item3.setQuantity(1);
          item3.setOrderedProductPrice(products.get(2).getSpecialPrice());

          OrderItem item4 = new OrderItem();
          item4.setProduct(products.get(3));
          item4.setQuantity(1);
          item4.setOrderedProductPrice(products.get(3).getSpecialPrice());

          OrderItem item5 = new OrderItem();
          item5.setProduct(products.get(4));
          item5.setQuantity(3);
          item5.setOrderedProductPrice(products.get(4).getSpecialPrice());

          OrderItem item6 = new OrderItem();
          item6.setProduct(products.get(5));
          item6.setQuantity(1);
          item6.setOrderedProductPrice(products.get(5).getSpecialPrice());

          OrderItem item7 = new OrderItem();
          item7.setProduct(products.get(6));
          item7.setQuantity(1);
          item7.setOrderedProductPrice(products.get(6).getSpecialPrice());

          OrderItem item8 = new OrderItem();
          item8.setProduct(products.get(7));
          item8.setQuantity(1);
          item8.setOrderedProductPrice(products.get(7).getSpecialPrice());

          OrderItem item9 = new OrderItem();
          item9.setProduct(products.get(8));
          item9.setQuantity(1);
          item9.setOrderedProductPrice(products.get(8).getSpecialPrice());

          OrderItem item10 = new OrderItem();
          item10.setProduct(products.get(9));
          item10.setQuantity(1);
          item10.setOrderedProductPrice(products.get(9).getSpecialPrice());

          orderItemRepo.saveAll(List.of(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10));
        }

        return null;
      });

      System.out.println("Seed data initialized successfully!");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
