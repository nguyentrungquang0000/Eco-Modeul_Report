package com.example.Report_T._controller;

import com.example.Report_T._service.ReportSubTable_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
public class ReportSubTableController {
    private final ReportSubTable_Service reportSubTableService;
    @GetMapping("/list/{report-template-id}")
    public ResponseEntity<?> getListByRpId(@PathVariable("report-template-id") String reportTemplateId) {
        return reportSubTableService.getListByRpId(reportTemplateId);
    }

    @DeleteMapping("/{table-id}")
    public ResponseEntity<?> deleteById(@PathVariable ("table-id") String tableId) {
        return reportSubTableService.deleteById(tableId);
    }
}
