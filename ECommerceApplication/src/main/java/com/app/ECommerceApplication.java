package com.app;

import java.time.LocalDate;
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
import com.app.entites.Payment;
import com.app.entites.Product;
import com.app.entites.Promo;
import com.app.entites.Role;
import com.app.entites.User;
import com.app.entites.Wishlist;
import com.app.repositories.AddressRepo;
import com.app.repositories.CategoryRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.PromoRepo;
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
    private ProductRepo productRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private PromoRepo promoRepo;

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

                if (userRepo.count() == 0) {
                    // Because we are inside a transaction, these fetches return MANAGED entities
                    Role adminRole = roleRepo.findById(AppConstants.ADMIN_ID).orElseThrow();
                    Role userRole = roleRepo.findById(AppConstants.USER_ID).orElseThrow();
                    Address address1 = addressRepo.findAll().get(0);
                    Address address2 = addressRepo.findAll().get(1);

                    User admin = new User();
                    admin.setFirstName("Admin");
                    admin.setLastName("Userr");
                    admin.setEmail("admin@example.com");
                    admin.setMobileNumber("1234567890");
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    admin.setRoles(Set.of(adminRole));
                    admin.setAddresses(List.of(address1));

                    Cart adminCart = new Cart();
                    adminCart.setUser(admin);
                    admin.setCart(adminCart);

                    Wishlist adminWishlist = new Wishlist();
                    adminWishlist.setUser(admin);
                    admin.setWishlist(adminWishlist);

                    User user1 = new User();
                    user1.setFirstName("Johnny");
                    user1.setLastName("Doenat");
                    user1.setEmail("john.doe@example.com");
                    user1.setMobileNumber("9876543210");
                    user1.setPassword(passwordEncoder.encode("user123"));
                    user1.setRoles(Set.of(userRole));
                    user1.setAddresses(List.of(address2));

                    Cart user1Cart = new Cart();
                    user1Cart.setUser(user1);
                    user1.setCart(user1Cart);

                    Wishlist user1Wishlist = new Wishlist();
                    user1Wishlist.setUser(user1);
                    user1.setWishlist(user1Wishlist);

                    User user2 = new User();
                    user2.setFirstName("Janette");
                    user2.setLastName("Smith");
                    user2.setEmail("jane.smith@example.com");
                    user2.setMobileNumber("5555555555");
                    user2.setPassword(passwordEncoder.encode("user123"));
                    user2.setRoles(Set.of(userRole));
                    user2.setAddresses(List.of(address1, address2));

                    Cart user2Cart = new Cart();
                    user2Cart.setUser(user2);
                    user2.setCart(user2Cart);

                    Wishlist user2Wishlist = new Wishlist();
                    user2Wishlist.setUser(user2);
                    user2.setWishlist(user2Wishlist);

                    userRepo.saveAll(List.of(admin, user1, user2));
                }

                if (productRepo.count() == 0) {
                    Category shoesCategory = categoryRepo.findAll().stream()
                            .filter(c -> c.getCategoryName().equals("Shoes"))
                            .findFirst().orElseThrow();
                    Category socksCategory = categoryRepo.findAll().stream()
                            .filter(c -> c.getCategoryName().equals("Socks"))
                            .findFirst().orElseThrow();

                    Product product1 = new Product();
                    product1.setProductName("Nike Air Max");
                    product1.setDescription("Premium running shoes with air cushioning");
                    product1.setPrice(1500000);
                    product1.setDiscount(10);
                    product1.setSpecialPrice(1350000);
                    product1.setQuantity(50);
                    product1.setImage("nike-air-max.jpg");
                    product1.setCategory(shoesCategory);

                    Product product2 = new Product();
                    product2.setProductName("Adidas Superstar");
                    product2.setDescription("Classic sneakers with shell toe design");
                    product2.setPrice(1200000);
                    product2.setDiscount(15);
                    product2.setSpecialPrice(1020000);
                    product2.setQuantity(30);
                    product2.setImage("adidas-superstar.jpg");
                    product2.setCategory(shoesCategory);

                    Product product3 = new Product();
                    product3.setProductName("Cotton Sports Socks");
                    product3.setDescription("Comfortable cotton socks for daily wear");
                    product3.setPrice(50000);
                    product3.setDiscount(5);
                    product3.setSpecialPrice(47500);
                    product3.setQuantity(100);
                    product3.setImage("cotton-socks.jpg");
                    product3.setCategory(socksCategory);

                    Product product4 = new Product();
                    product4.setProductName("Compression Running Socks");
                    product4.setDescription("High-performance compression socks for athletes");
                    product4.setPrice(150000);
                    product4.setDiscount(20);
                    product4.setSpecialPrice(120000);
                    product4.setQuantity(75);
                    product4.setImage("compression-socks.jpg");
                    product4.setCategory(socksCategory);

                    productRepo.saveAll(List.of(product1, product2, product3, product4));
                }

                if (paymentRepo.count() == 0) {
                    Payment paymentBca = new Payment();
                    paymentBca.setBankName("Bank BCA");
                    paymentBca.setStoreAccountNumber("0000000000");
                    paymentRepo.save(paymentBca);

                    Payment paymentMandiri = new Payment();
                    paymentMandiri.setBankName("Bank Mandiri");
                    paymentMandiri.setStoreAccountNumber("1111111111");
                    paymentRepo.save(paymentMandiri);

                    Payment paymentBni = new Payment();
                    paymentBni.setBankName("Bank BNI");
                    paymentBni.setStoreAccountNumber("2222222222");
                    paymentRepo.save(paymentBni);

                    Payment paymentBri = new Payment();
                    paymentBri.setBankName("Bank BRI");
                    paymentBri.setStoreAccountNumber("3333333333");
                    paymentRepo.save(paymentBri);
                }

                if (promoRepo.count() == 0) {
                    Promo promo10 = new Promo();
                    promo10.setPromoCode("PROMO10");
                    promo10.setDiscount(10);
                    promo10.setExpiry(LocalDate.now().plusMonths(1));
                    promoRepo.save(promo10);

                    Promo promo20 = new Promo();
                    promo20.setPromoCode("PROMO20");
                    promo20.setDiscount(20);
                    promo20.setExpiry(LocalDate.now().plusMonths(1));
                    promoRepo.save(promo20);

                    Promo promo50 = new Promo();
                    promo50.setPromoCode("PROMO50");
                    promo50.setDiscount(50);
                    promo50.setExpiry(LocalDate.now().plusMonths(1));
                    promoRepo.save(promo50);
                }
                
                return null;
            });

            System.out.println("Seed data initialized successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}