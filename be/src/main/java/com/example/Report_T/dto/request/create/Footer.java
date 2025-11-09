package com.example.Report_T.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Footer {
    private String id;
    private String title;
    private String note;
    private String name;
    private float fontSize;
    private String fontName;
    private int index;
    private boolean visible;
}
