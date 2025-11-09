package com.example.Report_T._repo;

import com.example.Report_T.modal.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportTemple_Repo extends JpaRepository<ReportTemplate, String> {
    List<ReportTemplate> findByGroupName(String categoryName);
}
