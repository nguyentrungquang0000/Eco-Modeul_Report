package com.example.Report_T.dto.request;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportRequest {
    private String reportTempleId;
    private List<FieldRequest> fieldList;
    private List<FilterRequest> filterList;
}
