package com.example.Report_T.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Filter {
    private String id;
    private String alias;               // tên hiển thị
    private String fieldKey;            // tên cột DB
    private List<String> operatorList;  // danh sách toán tử
    private String valueType;
    private String defaultValue;
    private String defaultOperator;
}
