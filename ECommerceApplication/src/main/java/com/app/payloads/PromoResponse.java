package com.app.payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoResponse {
    private List<PromoDTO> content;
    private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	private boolean lastPage;
}
