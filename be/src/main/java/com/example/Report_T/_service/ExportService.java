package com.example.Report_T._service;

import com.example.Report_T.dto.request.FieldRequest;
import com.example.Report_T.dto.request.ReportRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final ReportTemple_Service reportTemple_Service;

    public ResponseEntity<byte[]> export(ReportRequest request) {
        try {
            // 1️⃣ Lấy dữ liệu DB
            List<Map<String, Object>> data = reportTemple_Service.getReport(request);
            if (data == null || data.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // 2️⃣ Sắp xếp fieldList theo index
            List<FieldRequest> sortedFields = request.getFieldList().stream()
                    .sorted(Comparator.comparing(FieldRequest::getIndex))
                    .toList();

            // 3️⃣ Tạo workbook Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Báo cáo");

            // === Style ===
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // === Header nhóm (row 0) và header cột (row 1) ===
            // === Header nhóm (row 0) và header cột (row 1) ===
            Row groupRow = sheet.createRow(0);
            Row headerRow = sheet.createRow(1);

            String currentGroup = null;
            int startCol = 0;

            for (int i = 0; i < sortedFields.size(); i++) {
                FieldRequest field = sortedFields.get(i);
                String groupName = field.getGroupName();
                String columnName = getColumnDisplayName(field.getFieldId(), data.get(0));

                // tạo header con
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(columnName);
                headerCell.setCellStyle(headerStyle);

                // nhóm hiện tại chưa khởi tạo
                if (currentGroup == null) {
                    currentGroup = groupName;
                    startCol = i;
                }

                // kiểm tra xem đã tới cột cuối cùng hoặc nhóm tiếp theo khác chưa
                boolean isLastColumn = (i == sortedFields.size() - 1);
                boolean groupChanged = isLastColumn ||
                        !Objects.equals(currentGroup, sortedFields.get(i + 1).getGroupName());

                if (groupChanged) {
                    int endCol = isLastColumn ? i : i;
                    // chỉ merge nếu có >= 2 cột cùng nhóm
                    if (endCol > startCol) {
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, startCol, endCol));
                    }

                    // tạo ô group header
                    Cell groupCell = groupRow.createCell(startCol);
                    groupCell.setCellValue(currentGroup);
                    groupCell.setCellStyle(headerStyle);

                    // reset group
                    currentGroup = isLastColumn ? null : sortedFields.get(i + 1).getGroupName();
                    startCol = i + 1;
                }
            }


            // 4️⃣ Ghi dữ liệu
            int rowNum = 2;
            for (Map<String, Object> row : data) {
                Row dataRow = sheet.createRow(rowNum++);
                for (int i = 0; i < sortedFields.size(); i++) {
                    FieldRequest field = sortedFields.get(i);
                    String colName = getColumnDisplayName(field.getFieldId(), data.get(0));
                    Object value = row.get(colName);
                    Cell cell = dataRow.createCell(i);
                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    } else {
                        cell.setCellValue("");
                    }
                    cell.setCellStyle(dataStyle);
                }
            }

            // 5️⃣ Auto-size các cột
            for (int i = 0; i < sortedFields.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // 6️⃣ Ghi workbook ra byte[]
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            byte[] bytes = out.toByteArray();

            // 7️⃣ Trả về file Excel
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // 🔹 Style header
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // 🔹 Style data
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // 🔹 Lấy tên hiển thị động (dựa vào key trong Map đầu tiên)
    private String getColumnDisplayName(String fieldId, Map<String, Object> sampleRow) {
        // Vì bạn không lưu cứng map fieldId -> tên hiển thị
        // nên ta tìm trong Map theo giá trị gần giống (nếu bạn có metadata riêng thì load từ DB)
        for (String key : sampleRow.keySet()) {
            if (key.toLowerCase().contains("điện thoại") && fieldId.contains("23bb")) return key;
            if (key.toLowerCase().contains("họ tên") && fieldId.contains("2b76")) return key;
            if (key.toLowerCase().contains("phòng ban") && fieldId.contains("8df7")) return key;
            if (key.toLowerCase().contains("giới tính") && fieldId.contains("ce55")) return key;
            if (key.toLowerCase().contains("ngày sinh") && fieldId.contains("d649")) return key;
            if (key.toLowerCase().contains("lương") && fieldId.contains("32a5")) return key;
        }
        return fieldId;
    }
}
