package com.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.Report;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.ReportDTO;
import com.app.repositories.ProductRepo;
import com.app.repositories.ReportRepo;

@Service
public class ReportServiceImpl implements ReportService {
  @Autowired
  private ReportRepo reportRepo;

  @Autowired
  private ProductRepo productRepo;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public ReportDTO createReport(Long productId, String description) {
    Report report = new Report();

    Product targetProduct = productRepo.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

    report.setProduct(targetProduct);
    report.setDescription(description);

    Report saved = reportRepo.save(report);
    return modelMapper.map(saved, ReportDTO.class);
  }

  @Override
  public List<ReportDTO> getReports(Long productId) {
    if (!productRepo.existsById(productId)) {
      throw new ResourceNotFoundException("Product", "productId", productId);
    }
    List<Report> reports = reportRepo.findByProductProductId(productId);

    return reports.stream()
        .map(report -> modelMapper.map(report, ReportDTO.class))
        .collect(Collectors.toList());
  }

}
