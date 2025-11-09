package com.example.Report_T._controller;

import com.example.Report_T._service.ReportTemple_Service;
import com.example.Report_T.dto.request.create.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportTemple_Service reportTemple_Service;

    @GetMapping
    public List<Map<String, Object>> getReport(@RequestBody Report request) {
        return reportTemple_Service.getReport(request);
    }

    @GetMapping("/{report-temple-id}")
    public ResponseEntity<?> getReportTemple(@PathVariable ("report-temple-id") String reportTempleId) {
        return reportTemple_Service.getReportTemple(reportTempleId);
    }

    @PostMapping()
    public ResponseEntity<?> createReportTemple(@RequestBody Report request) {
        return reportTemple_Service.createReportTemple(request);
    }

    @GetMapping("/category")
    public ResponseEntity<?> getReportCategory() {
        return reportTemple_Service.getReportCategory();
    }

    @GetMapping("/list/{category-name}")
    public ResponseEntity<?> getReportList(@PathVariable("category-name") String categoryName) {
        return reportTemple_Service.getReportList(categoryName);
    }
}
