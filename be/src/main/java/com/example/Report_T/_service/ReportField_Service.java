package com.example.Report_T._service;

import com.example.Report_T._repo.ReportField_Repo;
import com.example.Report_T._repo.ReportTemple_Repo;
import com.example.Report_T.dto.request.create.Field;
import com.example.Report_T.modal.ReportTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportField_Service {
    private final ReportField_Repo reportField_repo;
    private final ReportTemple_Repo reportTemple_repo;

    public ResponseEntity<?> getListByRpId(String reportTemplateId) {
        ReportTemplate reportTemplate = reportTemple_repo.findById(reportTemplateId).orElseThrow();
        List<Field> fields = reportTemplate.getFields().stream().map(
                reportField -> Field.builder()
                        .id(reportField.getId())
                        .alias(reportField.getAlias())
                        .fieldKey(reportField.getFieldKey())
                        .dataType(reportField.getDataType())
                        .weight(reportField.getWeight())
                        .visible(reportField.getVisible())
                        .fontSize(reportField.getFontSize())
                        .index(reportField.getIndex())
                        .groupName(reportField.getGroupName())
                        .build()
        ).toList();
        return ResponseEntity.ok(fields);
    }

    public ResponseEntity<?> deleteById(String fieldId) {
        reportField_repo.deleteById(fieldId);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
