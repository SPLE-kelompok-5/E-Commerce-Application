package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entites.Report;

public interface ReportRepo extends JpaRepository<Report, Long> {
  List<Report> findByProductProductId(Long productId);
}
