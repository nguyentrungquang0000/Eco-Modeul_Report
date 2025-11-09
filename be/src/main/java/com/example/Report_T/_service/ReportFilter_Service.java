package com.example.Report_T._service;

import com.example.Report_T._repo.ReportField_Repo;
import com.example.Report_T._repo.ReportFilter_Repo;
import com.example.Report_T._repo.ReportTemple_Repo;
import com.example.Report_T.dto.request.create.Filter;
import com.example.Report_T.modal.ReportTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportFilter_Service {
    private final ReportField_Repo reportField_Repo;
    private final ReportTemple_Repo reportTemple_Repo;
    private final ReportFilter_Repo reportFilter_Repo;

    public ResponseEntity<?> getListByRpId(String reportTemplateId) {
        ReportTemplate reportTemplate = reportTemple_Repo.findById(reportTemplateId).orElseThrow();
        List<Filter> filters = reportTemplate.getFilters().stream().map(
                reportFilter -> Filter.builder()
                        .id(reportFilter.getId())
                        .alias(reportFilter.getAlias())
                        .fieldKey(reportFilter.getFieldKey())
                        .operatorList(reportFilter.getOperatorList())
                        .valueType(reportFilter.getValueType())
                        .defaultValue(reportFilter.getDefaultValue())
                        .defaultOperator(reportFilter.getDefaultOperator())
                        .build()

        ).toList();
        return ResponseEntity.ok(filters);
    }

    public ResponseEntity<?> deleteById(String filterId) {
        reportFilter_Repo.deleteById(filterId);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
