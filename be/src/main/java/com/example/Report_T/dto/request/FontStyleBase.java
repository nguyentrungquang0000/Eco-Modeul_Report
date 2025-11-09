package com.example.Report_T.dto.request;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FontStyleBase {
    private String fontName;     // Tên font: "Times New Roman"
    private int fontSize;        // Cỡ chữ: 12, 14, ...
    private String fontStyle;    // Các kiểu: "BOLD,ITALIC,UNDERLINE"
}
