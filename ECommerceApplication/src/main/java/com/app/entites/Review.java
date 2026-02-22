package com.app.entites;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "product_id" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private Integer rating;

	@Column(length = 1000)
	private String comment;

	@Column(nullable = false)
	private LocalDateTime reviewDate;

	@PrePersist
	@PreUpdate
	public void updateReviewDate() {
		reviewDate = LocalDateTime.now();
	}
}