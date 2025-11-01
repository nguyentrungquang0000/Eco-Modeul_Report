package com.example.Report_T._controller;

import com.example.Report_T._service.ExportService;
import com.example.Report_T.dto.request.ReportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {
    private final ExportService exportService;
    @GetMapping
    public ResponseEntity<?> export(@RequestBody ReportRequest request) {
        return exportService.export(request);
    }
}
