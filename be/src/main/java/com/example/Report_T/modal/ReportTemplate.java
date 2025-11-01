package com.example.Report_T.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class ReportTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String mainTable;
    private String groupName;
    private String dbConnectionKey;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportSubTable> subTables;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportFiled> fileds;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportFilter> filters;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportGroup> groups;
}
