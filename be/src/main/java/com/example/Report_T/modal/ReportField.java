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
public class ReportField {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String alias;   // tên field
    private String fieldKey;    // tên colum
    private String dataType;    // kiểu dữ liệu
    private int weight;
    private Boolean visible;            // có hiển thị hay không
    private float fontSize;
    private int index;
    private String groupName;
    @ManyToOne
    @JoinColumn(name = "report_template_id")
    @JsonIgnore
    private ReportTemplate reportTemplate;
}
