package com.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Product;
import com.app.entites.Review;
import com.app.entites.User;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

	Optional<Review> findByUserAndProduct(User user, Product product);

	List<Review> findByProductOrderByReviewDateDesc(Product product);

	@Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.productId = ?1")
	Double findAverageRatingByProductId(Long productId);

	Long countByProduct_ProductId(Long productId);
}
