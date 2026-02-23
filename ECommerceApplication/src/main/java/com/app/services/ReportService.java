package com.app.services;

import java.util.List;

import com.app.payloads.ReportDTO;

public interface ReportService {
  public ReportDTO createReport(Long productId, String description);

  public List<ReportDTO> getReports(Long productId);
}
