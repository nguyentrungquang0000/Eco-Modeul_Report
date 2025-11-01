package com.example.Report_T.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class ReportSubTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String joinType;
    private String subTableName;
    private String joinOn;

    @ManyToOne
    @JoinColumn(name = "report_template_id")
    @JsonIgnore
    private ReportTemplate reportTemplate;
}
