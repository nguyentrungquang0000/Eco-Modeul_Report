package com.example.Report_T._controller;

import com.example.Report_T._service.ReportFilter_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class ReportFilterController {
    private final ReportFilter_Service reportFilterService;

    @GetMapping("/list/{report-template-id}")
    public ResponseEntity<?> getListByRpId(@PathVariable("report-template-id") String reportTemplateId) {
        return reportFilterService.getListByRpId(reportTemplateId);
    }

    @DeleteMapping("/{filter-id}")
    public ResponseEntity<?> deleteById(@PathVariable ("filter-id") String filterId) {
        return reportFilterService.deleteById(filterId);
    }
}
