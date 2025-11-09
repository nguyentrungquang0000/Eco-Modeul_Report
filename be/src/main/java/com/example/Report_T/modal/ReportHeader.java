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
public class ReportHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;        // nội dung tiêu đề, vd: "BÁO CÁO NHÂN VIÊN"
    private String fontName;     // font, vd: "times.ttf"
    private int fontSize;        // cỡ chữ, vd: 16
    private String alignment;    // LEFT | CENTER | RIGHT
    private float spacingAfter;  // khoảng cách phía dưới, vd: 10f
    private boolean bold;
    private int index;
    private boolean visible;

    @ManyToOne
    @JoinColumn(name = "report_template_id")
    @JsonIgnore
    private ReportTemplate reportTemplate;
}
