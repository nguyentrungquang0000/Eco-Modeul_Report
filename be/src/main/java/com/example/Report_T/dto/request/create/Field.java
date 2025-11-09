package com.example.Report_T.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Field {
    private String id;                  // id field
    private String alias;               // tên hiển thị (VD: "Họ tên")
    private String fieldKey;            // cột DB (VD: u.full_name)
    private String dataType;            // kiểu dữ liệu (String, Number, Date, ...)
    private Integer weight;             // độ rộng cột
    private Boolean visible;            // có hiển thị hay không
    private float fontSize;
    private int index;
    private String groupName;
}
