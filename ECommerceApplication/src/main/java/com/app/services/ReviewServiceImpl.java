package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Product;
import com.app.entites.Review;
import com.app.entites.User;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CreateReviewDTO;
import com.app.payloads.ReviewDTO;
import com.app.repositories.OrderRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.ReviewRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepo reviewRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Override
	public ReviewDTO addOrUpdateReview(String email, Long productId, CreateReviewDTO createReviewDTO) {
		validateRequester(email);

		User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (!hasPurchasedProduct(email, productId)) {
			throw new APIException("You can only review products that you have purchased");
		}

		Review review = reviewRepo.findByUserAndProduct(user, product).orElseGet(Review::new);
		review.setUser(user);
		review.setProduct(product);
		review.setRating(createReviewDTO.getRating());
		review.setComment(createReviewDTO.getComment());

		Review savedReview = reviewRepo.save(review);

		return mapToDTO(savedReview);
	}

	@Override
	public List<ReviewDTO> getReviewsByProduct(Long productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		return reviewRepo.findByProductOrderByReviewDateDesc(product).stream().map(this::mapToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Double getAverageRatingByProduct(Long productId) {
		if (!productRepo.existsById(productId)) {
			throw new ResourceNotFoundException("Product", "productId", productId);
		}

		Double averageRating = reviewRepo.findAverageRatingByProductId(productId);

		return averageRating == null ? 0.0 : averageRating;
	}

	private boolean hasPurchasedProduct(String email, Long productId) {
		List<Order> orders = orderRepo.findAllByEmail(email);

		for (Order order : orders) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem.getProduct() != null && orderItem.getProduct().getProductId().equals(productId)) {
					return true;
				}
			}
		}

		return false;
	}

	private void validateRequester(String email) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new APIException("Unauthorized request");
		}

		String authenticatedEmail = String.valueOf(authentication.getPrincipal());

		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

		if (!isAdmin && !authenticatedEmail.equalsIgnoreCase(email)) {
			throw new APIException("You can only create a review using your own account");
		}
	}

	private ReviewDTO mapToDTO(Review review) {
		ReviewDTO reviewDTO = new ReviewDTO();

		reviewDTO.setReviewId(review.getReviewId());
		reviewDTO.setProductId(review.getProduct().getProductId());
		reviewDTO.setUserEmail(review.getUser().getEmail());
		reviewDTO.setRating(review.getRating());
		reviewDTO.setComment(review.getComment());
		reviewDTO.setReviewDate(review.getReviewDate());

		return reviewDTO;
	}
}
