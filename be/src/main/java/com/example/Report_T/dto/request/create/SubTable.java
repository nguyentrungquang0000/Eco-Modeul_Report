package com.example.Report_T.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubTable {
    private String id;
    private String tableName;
    private String joinType;        // INNER / LEFT / RIGHT
    private String joinOn;
}
