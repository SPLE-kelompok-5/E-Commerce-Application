package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.CreateReviewDTO;
import com.app.payloads.ReviewDTO;
import com.app.services.ReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@PostMapping("/public/users/{email}/products/{productId}/review")
	public ResponseEntity<ReviewDTO> addOrUpdateReview(@PathVariable String email,
			@PathVariable Long productId,
			@Valid @RequestBody CreateReviewDTO createReviewDTO) {

		ReviewDTO reviewDTO = reviewService.addOrUpdateReview(email, productId, createReviewDTO);

		return new ResponseEntity<ReviewDTO>(reviewDTO, HttpStatus.CREATED);
	}

	@GetMapping("/public/products/{productId}/reviews")
	public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable Long productId) {
		List<ReviewDTO> reviewDTOs = reviewService.getReviewsByProduct(productId);

		return new ResponseEntity<List<ReviewDTO>>(reviewDTOs, HttpStatus.OK);
	}

	@GetMapping("/public/products/{productId}/reviews/average-rating")
	public ResponseEntity<Double> getAverageRatingByProduct(@PathVariable Long productId) {
		Double averageRating = reviewService.getAverageRatingByProduct(productId);

		return new ResponseEntity<Double>(averageRating, HttpStatus.OK);
	}
}
