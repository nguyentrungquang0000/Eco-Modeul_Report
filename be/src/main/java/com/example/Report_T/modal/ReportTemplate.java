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

    private String pageType;  //a4-ngang-LANDSCAPE -dọc-PORTRAIT
    private float marginLeft;
    private float marginRight;
    private float marginTop;
    private float marginBottom;

    private String orderBy;

    private boolean showIndex;
    private float weightIndex;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportSubTable> subTables;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportField> Fields;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportFilter> filters;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportHeader> headers;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportFooter> footers;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportGroupBy> groupBys;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.REMOVE)
    private List<ReportOrderBy> orderBys;
}
