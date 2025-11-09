package com.example.Report_T.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderBy {
    private String id;
    private String title;       // tiêu đề phần order
    private String fieldKey;    // tên trường trong DB
    private String orderType;       // "ASC" hoặc "DESC"
    private boolean visible;
    private int index;
}
