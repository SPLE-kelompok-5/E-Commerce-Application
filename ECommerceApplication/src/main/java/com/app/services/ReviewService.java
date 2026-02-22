package com.app.services;

import java.util.List;

import com.app.payloads.CreateReviewDTO;
import com.app.payloads.ReviewDTO;

public interface ReviewService {

	ReviewDTO addOrUpdateReview(String email, Long productId, CreateReviewDTO createReviewDTO);

	List<ReviewDTO> getReviewsByProduct(Long productId);

	Double getAverageRatingByProduct(Long productId);
}
