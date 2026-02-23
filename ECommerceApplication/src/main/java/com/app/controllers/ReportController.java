package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.CartDTO;
import com.app.payloads.ReportDTO;
import com.app.services.CartService;
import com.app.services.ReportService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class ReportController {

  @Autowired
  private ReportService reportService;

  @PostMapping("/public/products/{productId}/report")
  public ResponseEntity<ReportDTO> reportProduct(@PathVariable Long productId, @RequestBody String description) {
    ReportDTO created = reportService.createReport(productId, description);

    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/admin/products/{productId}/reports")
  public ResponseEntity<List<ReportDTO>> fetchReports(@PathVariable Long productId) {
    List<ReportDTO> reports = reportService.getReports(productId);

    return new ResponseEntity<>(reports, HttpStatus.OK);
  }
}
