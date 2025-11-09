package com.example.Report_T._service;

import com.example.Report_T._repo.ReportSubTable_Repo;
import com.example.Report_T._repo.ReportTemple_Repo;
import com.example.Report_T.dto.request.create.SubTable;
import com.example.Report_T.modal.ReportTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportSubTable_Service {
    private final ReportSubTable_Repo reportSubTable_Repo;
    private final ReportTemple_Repo reportTemple_Repo;
    public ResponseEntity<?> getListByRpId(String reportTemplateId) {
        ReportTemplate reportTemplate = reportTemple_Repo.findById(reportTemplateId).orElseThrow();

        List<SubTable> tables = reportTemplate.getSubTables().stream().map(
                reportSubTable -> SubTable.builder()
                        .id(reportSubTable.getId())
                        .tableName(reportSubTable.getSubTableName())
                        .joinType(reportSubTable.getJoinType())
                        .joinOn(reportSubTable.getJoinOn())
                        .build()
        ).toList();
        return ResponseEntity.ok(tables);
    }

    public ResponseEntity<?> deleteById(String tableId) {
        reportSubTable_Repo.deleteById(tableId);
        return ResponseEntity.ok("Delete Success");
    }
}
