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
public class Report {
    private Info info;
    private List<SubTable> tables;
    private List<Field> fields;
    private List<Filter> filters;
    private List<Header> headerList;
    private List<Footer> footerList;
    private List<OrderBy> orderByList;
    private List<GroupBy> groupByList;
}
