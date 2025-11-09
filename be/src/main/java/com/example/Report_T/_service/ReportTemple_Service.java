package com.example.Report_T._service;

import com.example.Report_T._repo.*;
import com.example.Report_T.dto.request.create.*;
import com.example.Report_T.dto.response.ReportNode;
import com.example.Report_T.modal.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportTemple_Service {
    private final JdbcTemplate jdbcTemplate;

    private final ReportFilter_Repo reportFilter_Repo;
    private final ReportTemple_Repo reportTemple_Repo;
    private final ReportField_Repo reportField_Repo;
    private final ReportSubTable_Repo reportSubTable_Repo;
    private final ReportFooter_Repo reportFooter_Repo;
    private final ReportHeader_Repo reportHeader_Repo;
    private final ReportGroup_Repo reportGroup_Repo;
    private final ReportOrderBy_Repo reportOrderBy_Repo;

    public ResponseEntity<?> getReportTemple(String reportTempleId) {
        ReportTemplate reportTemplate =reportTemple_Repo.findById(reportTempleId).orElse(null);
        Info info = Info.builder()
                .id(reportTemplate.getId())
                .name(reportTemplate.getName())
                .groupName(reportTemplate.getGroupName())
                .mainTable(reportTemplate.getMainTable())
                .dbConnectionKey(reportTemplate.getDbConnectionKey())
                .pageType(reportTemplate.getPageType())
                .marginLeft(reportTemplate.getMarginLeft())
                .marginRight(reportTemplate.getMarginRight())
                .marginTop(reportTemplate.getMarginTop())
                .marginBottom(reportTemplate.getMarginBottom())
                .showIndex(reportTemplate.isShowIndex())
                .weightIndex(reportTemplate.getWeightIndex())
                .build();
        List<SubTable> tables = reportTemplate.getSubTables().stream().map(
                reportSubTable -> SubTable.builder()
                        .id(reportSubTable.getId())
                        .tableName(reportSubTable.getSubTableName())
                        .joinType(reportSubTable.getJoinType())
                        .joinOn(reportSubTable.getJoinOn())
                        .build()
        ).toList();
        List<Field> fields = reportTemplate.getFields().stream().map(
                reportField -> Field.builder()
                        .id(reportField.getId())
                        .alias(reportField.getAlias())
                        .fieldKey(reportField.getFieldKey())
                        .dataType(reportField.getDataType())
                        .weight(reportField.getWeight())
                        .visible(reportField.getVisible())
                        .fontSize(reportField.getFontSize())
                        .index(reportField.getIndex())
                        .groupName(reportField.getGroupName())
                        .build()
        ).toList();
        List<Filter> filters = reportTemplate.getFilters().stream().map(
                reportFilter -> Filter.builder()
                        .id(reportFilter.getId())
                        .alias(reportFilter.getAlias())
                        .fieldKey(reportFilter.getFieldKey())
                        .operatorList(reportFilter.getOperatorList())
                        .valueType(reportFilter.getValueType())
                        .defaultValue(reportFilter.getDefaultValue())
                        .defaultOperator(reportFilter.getDefaultOperator())
                        .build()

        ).toList();
        List<Footer> footerList = reportTemplate.getFooters().stream().map(
                reportFooter -> Footer.builder()
                        .id(reportFooter.getId())
                        .title(reportFooter.getTitle())
                        .name(reportFooter.getName())
                        .fontSize(reportFooter.getFontSize())
                        .fontName(reportFooter.getFontName())
                        .visible(reportFooter.isVisible())
                        .index(reportFooter.getIndex())
                        .note(reportFooter.getNote())
                        .build()
        ).toList();
        List<Header> headerList = reportTemplate.getHeaders().stream().map(
                reportHeader -> Header.builder()
                        .id(reportHeader.getId())
                        .title(reportHeader.getTitle())
                        .fontName(reportHeader.getFontName())
                        .fontSize(reportHeader.getFontSize())
                        .alignment(reportHeader.getAlignment())
                        .spacingAfter(reportHeader.getSpacingAfter())
                        .bold(reportHeader.isBold())
                        .index(reportHeader.getIndex())
                        .visible(reportHeader.isVisible())
                        .build()
        ).toList();

        List<GroupBy> groupByList = reportTemplate.getGroupBys().stream().map(
                reportGroupBy -> GroupBy.builder()
                        .id(reportGroupBy.getId())
                        .alias(reportGroupBy.getAlias())
                        .fieldKey(reportGroupBy.getFieldKey())
                        .orderType(reportGroupBy.getOrderType())
                        .visible(reportGroupBy.isVisible())
                        .index(reportGroupBy.getIndex())
                        .build()

        ).toList();

        List<OrderBy> orderByList = reportTemplate.getOrderBys().stream().map(
                    reportOrderBy -> OrderBy.builder()
                            .id(reportOrderBy.getId())
                            .title(reportOrderBy.getTitle())
                            .fieldKey(reportOrderBy.getFieldKey())
                            .orderType(reportOrderBy.getOrderType())
                            .visible(reportOrderBy.isVisible())
                            .index(reportOrderBy.getIndex())
                            .build()
                ).toList();
        Report report = Report.builder()
                .info(info)
                .tables(tables)
                .fields(fields)
                .filters(filters)
                .footerList(footerList)
                .headerList(headerList)
                .groupByList(groupByList)
                .orderByList(orderByList)
                .build();
        return ResponseEntity.ok(report);
    }

    @Transactional
    public ResponseEntity<?> createReportTemple(Report request) {
        Info info = request.getInfo();
        ReportTemplate reportTemplate = ReportTemplate.builder()
                .id(info.getId())
                .name(info.getName())
                .groupName(info.getGroupName())
                .mainTable(info.getMainTable())
                .dbConnectionKey(info.getDbConnectionKey())
                .pageType(info.getPageType())
                .marginLeft(info.getMarginLeft())
                .marginRight(info.getMarginRight())
                .marginTop(info.getMarginTop())
                .marginBottom(info.getMarginBottom())
                .showIndex(info.isShowIndex())
                .weightIndex(info.getWeightIndex())
                .build();
        reportTemplate = reportTemple_Repo.save(reportTemplate);

        List<SubTable> subTables = request.getTables();
        for (SubTable subTable : subTables){
            ReportSubTable table = ReportSubTable.builder()
                    .id(subTable.getId())
                    .joinType(subTable.getJoinType())
                    .subTableName(subTable.getTableName())
                    .joinOn(subTable.getJoinOn())
                    .reportTemplate(reportTemplate)
                    .build();
            reportSubTable_Repo.save(table);
        }

        List<Field> fields = request.getFields();
        for (Field field : fields){
            ReportField reportField = ReportField.builder()
                    .id(field.getId())
                    .alias(field.getAlias())
                    .fieldKey(field.getFieldKey())
                    .dataType(field.getDataType())
                    .weight(field.getWeight())
                    .visible(field.getVisible())
                    .fontSize(field.getFontSize())
                    .index(field.getIndex())
                    .groupName(field.getGroupName())
                    .reportTemplate(reportTemplate)
                    .build();
            reportField_Repo.save(reportField);
        }

        List<Filter> filters = request.getFilters();
        for (Filter filter : filters){
            ReportFilter reportFilter = ReportFilter.builder()
                    .id(filter.getId())
                    .alias(filter.getAlias())
                    .fieldKey(filter.getFieldKey())
                    .operatorList(filter.getOperatorList())
                    .valueType(filter.getValueType())
                    .defaultValue(filter.getDefaultValue())
                    .reportTemplate(reportTemplate)
                    .build();
            reportFilter_Repo.save(reportFilter);
        }
        for(Header header : request.getHeaderList()){
            ReportHeader reportHeader = ReportHeader.builder()
                    .id(header.getId())
                    .title(header.getTitle())
                    .fontName(header.getFontName())
                    .fontSize(header.getFontSize())
                    .alignment(header.getAlignment())
                    .spacingAfter(header.getSpacingAfter())
                    .bold(header.isBold())
                    .index(header.getIndex())
                    .visible(header.isVisible())
                    .reportTemplate(reportTemplate)
                    .build();
            reportHeader_Repo.save(reportHeader);
        }
        for (Footer footer : request.getFooterList()){
            ReportFooter reportFooter = ReportFooter.builder()
                    .id(footer.getId())
                    .title(footer.getTitle())
                    .name(footer.getName())
                    .note(footer.getNote())
                    .fontSize(footer.getFontSize())
                    .fontName(footer.getFontName())
                    .index(footer.getIndex())
                    .visible(footer.isVisible())
                    .reportTemplate(reportTemplate)
                    .build();
            reportFooter_Repo.save(reportFooter);
        }
        for (GroupBy groupBy : request.getGroupByList()){
            ReportGroupBy reportGroupBy = ReportGroupBy.builder()
                    .id(groupBy.getId())
                    .alias(groupBy.getAlias())
                    .fieldKey(groupBy.getFieldKey())
                    .orderType(groupBy.getOrderType())
                    .visible(groupBy.isVisible())
                    .index(groupBy.getIndex())
                    .reportTemplate(reportTemplate)
                    .build();
            reportGroup_Repo.save(reportGroupBy);
        }

        for (OrderBy orderBy : request.getOrderByList()){
            ReportOrderBy reportOrderBy = ReportOrderBy.builder()
                    .id(orderBy.getId())
                    .title(orderBy.getTitle())
                    .fieldKey(orderBy.getFieldKey())
                    .orderType(orderBy.getOrderType())
                    .visible(orderBy.isVisible())
                    .index(orderBy.getIndex())
                    .reportTemplate(reportTemplate)
                    .build();
            reportOrderBy_Repo.save((reportOrderBy));
        }
        
        return ResponseEntity.ok(reportTemplate.getId());
    }

    public ResponseEntity<?> getReportCategory() {
        List<String> groupNames = jdbcTemplate.queryForList("SELECT DISTINCT group_name FROM report_template", String.class);
        List<Map<String, String>> result = groupNames.stream()
                .map(name -> Map.of("title", name))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getReportList(String categoryName) {
        List<ReportTemplate> reportTemplateList = reportTemple_Repo.findByGroupName(categoryName);
        List<ReportNode> response = reportTemplateList.stream().map(
                reportTemplate -> ReportNode.builder()
                        .id(reportTemplate.getId())
                        .title(reportTemplate.getName())
                        .category(reportTemplate.getGroupName())
                        .build()
        ).toList();
        return ResponseEntity.ok(response);
    }

    public List<Map<String, Object>> getReport(Report request) {
        //SELECT
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(createSelect(request));
        //FROM
        sql.append(createFrom(request));
        //WHERE
        sql.append(createFilter(request));
        //GROUP
        sql.append(createGroupBy(request));
        //ORDER
        sql.append(createOrderBy(request));
        System.out.println(sql);

        return jdbcTemplate.queryForList(sql.toString());
    }


    public String createSelect(Report report) {
        StringBuilder fieldSql = new StringBuilder();
        for (Field field : report.getFields().stream()
                .filter(Field::getVisible)
                .toList()) {
            fieldSql.append(field.getFieldKey()).append(" AS \"").append(field.getAlias()).append("\", ");
        }
        fieldSql.delete(fieldSql.length()-2, fieldSql.length());
        return fieldSql.toString();
    }

    public String createFrom(Report report){
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(" FROM ").append(report.getInfo().getMainTable()).append(" ");
        for (SubTable subTable : report.getTables()){
            fromSql.append(subTable.getJoinType()).append(" ").append(subTable.getTableName()).append(" ON ").append(subTable.getJoinOn()).append(" ");
        }
        fromSql.delete(fromSql.length()-1, fromSql.length());
        return fromSql.toString();
    }

    private String createFilter(Report report) {

        StringBuilder filterSql = new StringBuilder(" WHERE 1 = 1");
        for (Filter filter : report.getFilters()){
            if(filter.getDefaultOperator() == null || filter.getDefaultOperator().equals("")) continue;
            String fieldKey = filter.getFieldKey();
            String operator = filter.getDefaultOperator();
            String rawValue = filter.getDefaultValue();
            String formattedValue = "";

            switch (filter.getValueType().toUpperCase()) {
                case "LIST":
                    // Tách chuỗi "a,b,c" → ('a','b','c')
                    List<String> items = List.of(rawValue.split(","));
                    String joined = items.stream()
                            .map(String::trim)
                            .map(v -> "'" + v + "'")
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("");
                    formattedValue = "(" + joined + ")";
                    break;

                case "NUMBER":
                    // Không cần nháy, đảm bảo là số hợp lệ
                    formattedValue = rawValue.trim();
                    break;

                case "DATE":
                    // Giả sử ngày dạng yyyy-MM-dd → chuyển thành 'yyyy-MM-dd'
                    formattedValue = "'" + rawValue.trim() + "'";
                    break;

                default:
                    // Mặc định là chuỗi
                    formattedValue = "'" + rawValue.trim() + "'";
                    break;
            }
            filterSql.append(" AND ").append(fieldKey)
                    .append(" ").append(operator)
                    .append(" ").append(formattedValue);
        }
        return filterSql.toString();
    }

    public String createOrderBy(Report report){
        List<OrderBy> orderByList = report.getOrderByList().stream()
                .filter(OrderBy::isVisible)            // chỉ lấy visible = true
                .sorted(Comparator.comparingInt(OrderBy::getIndex)) // sắp xếp theo index
                .toList();
        if (orderByList.isEmpty()) return "";
        StringBuilder orderSql = new StringBuilder(" ORDER BY ");
        for (OrderBy orderBy : orderByList){
            orderSql.append(orderBy.getFieldKey()).append(" ").append(orderBy.getOrderType()).append(", ");
        }
        orderSql.delete(orderSql.length() - 2, orderSql.length());
        return orderSql.toString();
    }

    public String createGroupBy(Report report){
        List<GroupBy> groupByList = report.getGroupByList().stream()
                .filter(GroupBy::isVisible)            // chỉ lấy visible = true
                .sorted(Comparator.comparingInt(GroupBy::getIndex)) // sắp xếp theo index
                .toList();
        if (groupByList.isEmpty()) return "";
        StringBuilder orderSql = new StringBuilder(" GROUP BY ");
        for (GroupBy groupBy : groupByList){
            orderSql.append(groupBy.getFieldKey()).append(" ").append(groupBy.getOrderType());
        }

        return orderSql.toString();
    }
}
