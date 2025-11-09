package com.example.Report_T.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Info {
    private String id;
    private String name;
    private String groupName;
    private String mainTable;
    private String dbConnectionKey;
    private String pageType;
    private float marginLeft;
    private float marginRight;
    private float marginTop;
    private float marginBottom;
    private boolean showIndex;
    private float weightIndex;

}
