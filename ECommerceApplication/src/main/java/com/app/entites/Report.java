package com.app.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long reportId;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @NotBlank
  @Size(min = 6, message = "Report description must contain atleast 6 characters")
  private String description;
}
