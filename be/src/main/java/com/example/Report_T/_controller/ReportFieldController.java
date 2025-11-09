package com.example.Report_T._controller;

import com.example.Report_T._service.ReportField_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/field")
@RequiredArgsConstructor
public class ReportFieldController {
    private final ReportField_Service reportFieldService;
    @GetMapping("/list/{report-template-id}")
    public ResponseEntity<?> getListByRpId(@PathVariable("report-template-id") String reportTemplateId) {
        return reportFieldService.getListByRpId(reportTemplateId);
    }

    @DeleteMapping("/{field-id}")
    public ResponseEntity<?> deleteById(@PathVariable ("field-id") String fieldId) {
        return reportFieldService.deleteById(fieldId);
    }
}
