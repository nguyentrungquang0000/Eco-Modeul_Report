package com.example.Report_T._service;

import com.example.Report_T._repo.ReportFiled_Repo;
import com.example.Report_T._repo.ReportFilter_Repo;
import com.example.Report_T._repo.ReportSubTable_Repo;
import com.example.Report_T._repo.ReportTemple_Repo;
import com.example.Report_T.dto.request.FieldRequest;
import com.example.Report_T.dto.request.FilterRequest;
import com.example.Report_T.dto.request.ReportRequest;
import com.example.Report_T.modal.ReportFiled;
import com.example.Report_T.modal.ReportFilter;
import com.example.Report_T.modal.ReportSubTable;
import com.example.Report_T.modal.ReportTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportTemple_Service {
    private final JdbcTemplate jdbcTemplate;

    private final ReportFilter_Repo reportFilter_Repo;
    private final ReportTemple_Repo reportTemple_Repo;
    private final ReportFiled_Repo reportFiled_Repo;
    private final ReportSubTable_Repo reportSubTable_Repo;

    public List<Map<String, Object>> getReport(ReportRequest request) {
        //SELECT
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(createSelect(request));
        //FROM
        sql.append(createFrom(request));
        //WHERE
        sql.append(createFilter(request));
        //GROUP
        //ORDER
        System.out.println(sql);

        return jdbcTemplate.queryForList(sql.toString());
    }


    public String createSelect(ReportRequest request) {
        StringBuilder fieldSql = new StringBuilder();
        for (FieldRequest field : request.getFieldList()){
            ReportFiled reportFiled = reportFiled_Repo.findById(field.getFieldId()).orElseThrow();
            fieldSql.append(reportFiled.getFieldKey()).append(" AS \"").append(reportFiled.getAlias()).append("\", ");
        }

        fieldSql.delete(fieldSql.length()-2, fieldSql.length());
        return fieldSql.toString();
    }

    public String createFrom(ReportRequest request){
        ReportTemplate reportTemplate = reportTemple_Repo.findById(request.getReportTempleId()).orElseThrow();
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(" FROM ").append(reportTemplate.getMainTable()).append(" ");
        List<ReportSubTable> reportSubTables = reportSubTable_Repo.findByReportTemplate_Id(request.getReportTempleId());
        for (ReportSubTable reportSubTable : reportSubTables){
            fromSql.append(reportSubTable.getJoinType()).append(" ").append(reportSubTable.getSubTableName()).append(" ON ").append(reportSubTable.getJoinOn()).append(" ");
        }
        fromSql.delete(fromSql.length()-1, fromSql.length());
        return fromSql.toString();
    }

    private String createFilter(ReportRequest request) {

        StringBuilder filterSql = new StringBuilder(" WHERE 1 = 1");
        for (FilterRequest filter : request.getFilterList()){
            ReportFilter reportFilter = reportFilter_Repo.findById(filter.getFilterId()).orElseThrow();
            String fieldKey = reportFilter.getFieldKey();
            String operator = filter.getOperator();
            String rawValue = filter.getValue();
            String formattedValue = "";

            switch (reportFilter.getValueType().toUpperCase()) {
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


    public ResponseEntity<?> getReportTemple(String reportTempleId) {
        ReportTemplate reportTemplate =reportTemple_Repo.findById(reportTempleId).orElse(null);
        return ResponseEntity.ok(reportTemplate);
    }
}
