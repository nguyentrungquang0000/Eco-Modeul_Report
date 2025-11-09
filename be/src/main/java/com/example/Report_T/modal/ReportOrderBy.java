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
public class ReportOrderBy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;       // tiêu đề phần order
    private String fieldKey;    // tên trường trong DB
    private String orderType;       // "ASC" hoặc "DESC"
    private boolean visible;
    private int index;

    @ManyToOne
    @JoinColumn(name = "report_template_id")
    @JsonIgnore
    private ReportTemplate reportTemplate;
}
