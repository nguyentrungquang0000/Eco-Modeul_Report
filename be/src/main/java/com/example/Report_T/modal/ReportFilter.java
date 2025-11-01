package com.example.Report_T.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class ReportFilter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String alias;   // tên bộ lọc
    private String fieldKey;    // key bộ lọc
    private List<String> operatorList;  //in, between, > , < . =
    private String valueType;   // LIST, NUMBER, DATE
    private String defaultOperator;
    private String defaultValue;

    @ManyToOne
    @JoinColumn(name = "report_template_id")
    @JsonIgnore
    private ReportTemplate reportTemplate;
}
