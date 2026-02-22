package com.app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.CreditCard;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Payment;
import com.app.entites.Product;
import com.app.entites.StoreDiscount;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CreditCardDTO;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderItemDTO;
import com.app.payloads.OrderResponse;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.CreditCardRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.StoreDiscountRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    public UserRepo userRepo;

    @Autowired
    public CartRepo cartRepo;

    @Autowired
    public OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CreditCardRepo creditCardRepo;

    @Autowired
    private StoreDiscountRepo storeDiscountRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    public OrderItemRepo orderItemRepo;

    @Autowired
    public CartItemRepo cartItemRepo;

    @Autowired
    public UserService userService;

    @Autowired
    public CartService cartService;

    @Autowired
    public ModelMapper modelMapper;

    @Override
    public OrderDTO placeOrder(String email, Long cartId, String paymentMethod) {

        Cart cart = cartRepo.findCartByEmailAndCartId(email, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.size() == 0) {
            throw new APIException("Cart is empty");
        }

        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus("Order Accepted !");

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment = paymentRepo.save(payment);

        order.setPayment(payment);

        // Get active store discount (start <= current <= end)
        StoreDiscount activeDiscount = storeDiscountRepo.findActiveDiscount(LocalDate.now()).orElse(null);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            
            Product product = cartItem.getProduct();
            double basePrice = product.getPrice();
            double quantity = cartItem.getQuantity();
            
            // Apply store discount if active, otherwise use product's special price
            if (activeDiscount != null) {
                // Use store discount - apply to base price
                orderItem.setDiscount(activeDiscount.getDiscountPercentage());
                orderItem.setOrderedProductPrice(basePrice);
                
                double itemTotal = basePrice * quantity;
                double discountAmount = itemTotal * (activeDiscount.getDiscountPercentage() / 100.0);
                totalAmount += (itemTotal - discountAmount);
            } else {
                // Use product discount - use special_price
                orderItem.setDiscount(product.getDiscount());
                orderItem.setOrderedProductPrice(product.getSpecialPrice());
                
                totalAmount += product.getSpecialPrice() * quantity;
            }
            
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order = orderRepo.save(order);

        // Clear cart and update product quantities
        cartItems.forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            
            product.setQuantity(product.getQuantity() - quantity);
            productRepo.save(product);
            
            cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());
        });

        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO placeOrderWithCreditCard(String email, Long cartId, CreditCardDTO creditCardDTO) {
        Cart cart = cartRepo.findCartByEmailAndCartId(email, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        
        if (cart.getCartItems().isEmpty()) {
            throw new APIException("Cart is empty");
        }
        
        Payment payment = new Payment();
        payment.setPaymentMethod("CREDIT_CARD");
        payment = paymentRepo.save(payment);
        
        CreditCard creditCard = modelMapper.map(creditCardDTO, CreditCard.class);
        creditCard.setPayment(payment);
        creditCardRepo.save(creditCard);
        
        StoreDiscount activeDiscount = storeDiscountRepo.findActiveDiscount(LocalDate.now()).orElse(null);
        
        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setPayment(payment);
        order.setOrderStatus("Order Accepted !");
        
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;
        
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            
            Product product = cartItem.getProduct();
            double basePrice = product.getPrice();
            double quantity = cartItem.getQuantity();
     
            if (activeDiscount != null) {
                orderItem.setDiscount(activeDiscount.getDiscountPercentage());
                orderItem.setOrderedProductPrice(basePrice);
                
                double itemTotal = basePrice * quantity;
                double discountAmount = itemTotal * (activeDiscount.getDiscountPercentage() / 100.0);
                totalAmount += (itemTotal - discountAmount);
            } else {
                orderItem.setDiscount(product.getDiscount());
                orderItem.setOrderedProductPrice(product.getSpecialPrice());
                
                totalAmount += product.getSpecialPrice() * quantity;
            }
            
            orderItems.add(orderItem);
        }
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order = orderRepo.save(order);
        
        // Clear cart items and update product quantities
        List<CartItem> cartItemsCopy = new ArrayList<>(cart.getCartItems());
        for (CartItem item : cartItemsCopy) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepo.save(product);
            
            Long productId = product.getProductId();
            cartService.deleteProductFromCart(cartId, productId);
        }
        
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        
        List<OrderItemDTO> orderItemDTOs = orderItems.stream()
            .map(item -> modelMapper.map(item, OrderItemDTO.class))
            .collect(Collectors.toList());
        orderDTO.setOrderItems(orderItemDTOs);
        
        return orderDTO;
    }

    @Override
    public List<OrderDTO> getOrdersByUser(String email) {
        List<Order> orders = orderRepo.findAllByEmail(email);

        List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        if (orderDTOs.size() == 0) {
            throw new APIException("No orders placed yet by the user with email: " + email);
        }

        return orderDTOs;
    }

    @Override
    public OrderDTO getOrder(String email, Long orderId) {

        Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }

        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Order> pageOrders = orderRepo.findAll(pageDetails);

        List<Order> orders = pageOrders.getContent();

        List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
        
        if (orderDTOs.size() == 0) {
            throw new APIException("No orders placed yet by the users");
        }

        OrderResponse orderResponse = new OrderResponse();
        
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());
        
        return orderResponse;
    }

    @Override
    public OrderDTO updateOrder(String email, Long orderId, String orderStatus) {

        Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }

        order.setOrderStatus(orderStatus);

        return modelMapper.map(order, OrderDTO.class);
    }

}