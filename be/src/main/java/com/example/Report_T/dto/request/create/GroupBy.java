package com.example.Report_T.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GroupBy {
    private String id;
    private String alias;       // tiêu đề phần group
    private String fieldKey;    // tên trường trong DB
    private String orderType;       // "ASC" hoặc "DESC"
    private boolean visible;
    private int index;
}
