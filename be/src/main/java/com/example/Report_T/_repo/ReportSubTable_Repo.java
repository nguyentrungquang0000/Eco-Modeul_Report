package com.example.Report_T._repo;

import com.example.Report_T.modal.ReportSubTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportSubTable_Repo extends JpaRepository<ReportSubTable, String> {
    List<ReportSubTable> findByReportTemplate_Id(String reportTemplateId);
}
