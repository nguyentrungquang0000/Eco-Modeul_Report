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
public class ReportFiled {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String alias;   // tên field
    private String fieldKey;    // tên colum
    private String dataType;    // kiểu dữ liệu

    @ManyToOne
    @JoinColumn(name = "report_template_id")
    @JsonIgnore
    private ReportTemplate reportTemplate;
}
