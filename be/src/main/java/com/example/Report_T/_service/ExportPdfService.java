package com.example.Report_T._service;

import com.example.Report_T._repo.ReportField_Repo;
import com.example.Report_T.dto.request.create.Footer;
import com.example.Report_T.dto.request.create.Header;
import com.example.Report_T.dto.request.create.Field;
import com.example.Report_T.dto.request.create.Report;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportPdfService {
    private final ReportTemple_Service reportTemple_Service;
    private final ReportField_Repo reportField_Repo;
    public ResponseEntity<?> exportPdf(Report request) {
        try {
            // 1️⃣ Lấy dữ liệu DB
            List<Map<String, Object>> data = reportTemple_Service.getReport(request);
            if (data == null || data.isEmpty()) {
                // Tạo file PDF trống (hợp lệ, không lỗi)
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try (baos) {
                    com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                    com.itextpdf.text.pdf.PdfWriter.getInstance(document, baos);
                    document.open();
                    document.add(new com.itextpdf.text.Paragraph("Khong co du lieu."));
                    document.add(new com.itextpdf.text.Paragraph("Hay xem lai bo loc."));
                    document.close();
                }

                byte[] bytes = baos.toByteArray();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(bytes);
            }


            // 2️⃣ Sắp xếp fieldList theo index
            List<Field> sortedFields = request.getFields().stream()
                    .filter(f -> f.getWeight() > 0)
                    .filter(Field::getVisible)
                    .sorted(Comparator.comparing(Field::getIndex))
                    .toList();

            // 3️⃣ Tạo PDF
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(request.getInfo().getPageType().equals("LANDSCAPE") ? PageSize.A4.rotate() : PageSize.A4 ,
                    request.getInfo().getMarginLeft(), request.getInfo().getMarginRight(), request.getInfo().getMarginTop(), request.getInfo().getMarginBottom()); // lề: top, right, bottom, left
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // Footer
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_RIGHT,
                            new Phrase("Trang " + writer.getPageNumber(), FontFactory.getFont(FontFactory.HELVETICA, 9)),
                            document.right() - 20,
                            document.bottom() - 20, 0);
                }
            });

            document.open();

            // Font Unicode Việt Nam
            BaseFont baseFont = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12);

            // Header
            if (request.getHeaderList() != null && !request.getHeaderList().isEmpty()) {
                for (Header headerLine : request.getHeaderList().stream()
                        .filter(Header::isVisible)
                        .sorted(Comparator.comparing(Header::getIndex))
                        .toList()) {
                    // Tạo font riêng cho mỗi dòng header
//                    BaseFont baseFontHeader = BaseFont.createFont(
//                            "fonts/" + (headerLine.getFontName() != null ? headerLine.getFontName() : "times.ttf"),
//                            BaseFont.IDENTITY_H, BaseFont.EMBEDDED
//                    );
                    BaseFont baseFontHeader = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

                    int style = headerLine.isBold() ? Font.BOLD : Font.NORMAL;
                    Font headerFont = new Font(baseFontHeader, headerLine.getFontSize(), style);

                    Paragraph header = new Paragraph(headerLine.getTitle(), headerFont);

                    // Căn lề
                    switch (headerLine.getAlignment().toUpperCase()) {
                        case "LEFT" -> header.setAlignment(Element.ALIGN_LEFT);
                        case "RIGHT" -> header.setAlignment(Element.ALIGN_RIGHT);
                        default -> header.setAlignment(Element.ALIGN_CENTER);
                    }

                    header.setSpacingAfter(headerLine.getSpacingAfter());
                    document.add(header);
                }
            }

            // 4️⃣ Tạo bảng
            int totalColumns = sortedFields.size() + (request.getInfo().isShowIndex() ? 1 : 0);
            PdfPTable table = new PdfPTable(totalColumns);
            table.setWidthPercentage(100);

// 👉 Set weight động cho cột
            float[] columnWidths = new float[totalColumns];
            int offset = 0;
            if (request.getInfo().isShowIndex()) {
                columnWidths[0] = request.getInfo().getWeightIndex();
                offset = 1;
            }
            for (int i = 0; i < sortedFields.size(); i++) {
                columnWidths[i + offset] =
                        sortedFields.get(i).getWeight() > 0 ? sortedFields.get(i).getWeight() : 1f;
            }
            table.setWidths(columnWidths);

// 4.1️⃣ Group header
            String currentGroup = null;
            int groupStart = request.getInfo().isShowIndex() ? 1 : 0;
            if (request.getInfo().isShowIndex()) {
                PdfPCell sttHeader = new PdfPCell(new Phrase("STT", new Font(baseFont, 12, Font.BOLD)));
                sttHeader.setRowspan(2);
                sttHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(sttHeader);
            }
            for (int i = 0; i < sortedFields.size(); i++) {
                Field field = sortedFields.get(i);
                String groupName = field.getGroupName();
                if (currentGroup == null) {
                    currentGroup = groupName;
                    groupStart = i + offset;
                }

                boolean lastColumn = (i == sortedFields.size() - 1);
                boolean groupChanged = lastColumn
                        || !currentGroup.equals(sortedFields.get(i + 1).getGroupName())
                        || sortedFields.get(i + 1).getGroupName().equals("");


                if (groupChanged) {
                    PdfPCell groupCell = new PdfPCell(new Phrase(currentGroup, font));
                    groupCell.setColspan(i - (groupStart - offset) + 1);
                    groupCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(groupCell);

                    currentGroup = lastColumn ? null : sortedFields.get(i + 1).getGroupName();
                    groupStart = i + 1 + offset;
                }
            }


// 4.2️⃣ Column headers
            for (Field field : sortedFields) {
                String colName = field.getAlias() != null ? field.getAlias() : field.getId();
                Font cellFont = new Font(baseFont,
                        field.getFontSize() > 0 ? field.getFontSize() : 10,
                        Font.BOLD);
                PdfPCell cell = new PdfPCell(new Phrase(colName, cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            table.setHeaderRows(2);

// 4.3️⃣ Dữ liệu
            int rowIndex = 1;
            for (Map<String, Object> row : data) {
                if (request.getInfo().isShowIndex()) {
                    Font indexFont = new Font(baseFont, 12, Font.NORMAL);
                    PdfPCell indexCell = new PdfPCell(new Phrase(String.valueOf(rowIndex++), indexFont));
                    indexCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(indexCell);
                }

                for (Field field : sortedFields) {
                    String colName = field.getAlias() != null ? field.getAlias() : field.getId();
                    Object value = row.get(colName);
                    Font cellFont = new Font(baseFont,
                            field.getFontSize() > 0 ? field.getFontSize() : 10,
                            Font.NORMAL);
                    PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : "", cellFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }


            document.add(table);
            //----------footer
            if (request.getFooterList() != null && !request.getFooterList().isEmpty()) {
                document.add(Chunk.NEWLINE);

                List<Footer> footers = request.getFooterList().stream()
                        .filter(Footer::isVisible) // chỉ lấy footer có visible = true
                        .sorted(Comparator.comparingInt(Footer::getIndex)) // sắp xếp theo index
                        .collect(Collectors.toCollection(ArrayList::new)); // tạo danh sách mutable


                int footerCount = footers.size();
                if (footerCount > 0) {
                    PdfPTable footerTable = new PdfPTable(footerCount);
                    footerTable.setWidthPercentage(100);
                    if (footerCount == 1) {
                        footerTable.setWidthPercentage(40); // bảng nhỏ hơn
                        footerTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    } else {
                        footerTable.setWidthPercentage(100);
                        footerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }

                    for (Footer footer : footers) {
                        PdfPCell cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                        Font titleFont = new Font(baseFont, footer.getFontSize() > 0 ? footer.getFontSize() : 12, Font.BOLD);
                        Font noteFont = new Font(baseFont, footer.getFontSize() > 1 ? footer.getFontSize() - 1 : 10, Font.ITALIC);
                        Font nameFont = new Font(baseFont, footer.getFontSize() > 0 ? footer.getFontSize() : 12, Font.BOLD);

                        // Title
                        if (footer.getTitle() != null && !footer.getTitle().isEmpty()) {
                            Paragraph titlePara = new Paragraph(footer.getTitle(), titleFont);
                            titlePara.setAlignment(Element.ALIGN_CENTER);
                            cell.addElement(titlePara);
                        }

                        // Note (bé bé nghiêng)
                        if (footer.getNote() != null && !footer.getNote().isEmpty()) {
                            Paragraph notePara = new Paragraph(footer.getNote(), noteFont);
                            notePara.setAlignment(Element.ALIGN_CENTER);
                            cell.addElement(notePara);
                        }

                        // Chừa chỗ để ký
                        cell.addElement(new Paragraph("\n\n\n\n"));

                        // Tên người ký
                        if (footer.getName() != null && !footer.getName().isEmpty()) {
                            Paragraph namePara = new Paragraph(footer.getName(), nameFont);
                            namePara.setAlignment(Element.ALIGN_CENTER);
                            cell.addElement(namePara);
                        }

                        footerTable.addCell(cell);
                    }

                    document.add(footerTable);
                }
            }

            document.close();

            byte[] bytes = out.toByteArray();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }


}
