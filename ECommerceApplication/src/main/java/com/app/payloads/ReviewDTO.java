package com.app.payloads;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

	private Long reviewId;
	private Long productId;
	private String userEmail;
	private Integer rating;
	private String comment;
	private LocalDateTime reviewDate;
}
