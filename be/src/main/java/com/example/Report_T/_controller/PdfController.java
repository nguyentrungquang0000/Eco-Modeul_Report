package com.example.Report_T._controller;

import com.example.Report_T._service.ExportPdfService;
import com.example.Report_T.dto.request.create.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PdfController {
    private final ExportPdfService exportPdfService;

    @PostMapping
    public ResponseEntity<?> exportPdf(@RequestBody Report request) {
        return exportPdfService.exportPdf(request);
    }
}
