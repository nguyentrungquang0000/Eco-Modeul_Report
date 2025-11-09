package com.example.Report_T.Utils;

import com.example.Report_T.dto.request.FontStyleBase;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FontUtils {
    private static final String FONT_DIR = "src/main/resources/fonts/";

    public static Font getFont(FontStyleBase fontStyleBase) {
        try {
            String fileName = resolveFontFile(fontStyleBase.getFontName());
            BaseFont baseFont = BaseFont.createFont(
                    FONT_DIR + fileName,
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
            int fontStyle = parseStyle(fontStyleBase.getFontStyle());

            return new Font(baseFont, fontStyleBase.getFontSize(), fontStyle);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font(Font.FontFamily.HELVETICA, fontStyleBase.getFontSize(), Font.NORMAL);
        }
    }

    private static String resolveFontFile(String fontName) {
        if (fontName == null) return "times.ttf";
        fontName = fontName.toLowerCase();

        if (fontName.contains("arial")) return "arial.ttf";
        if (fontName.contains("times")) return "times.ttf";
        if (fontName.contains("tahoma")) return "tahoma.ttf";
        if (fontName.contains("calibri")) return "calibri.ttf";
        if (fontName.contains("verdana")) return "verdana.ttf";
        if (fontName.contains("georgia")) return "georgia.ttf";

        return "times.ttf";
    }

    private static int parseStyle(String styleList) {
        if (styleList == null || styleList.trim().isEmpty()) return Font.NORMAL;

        List<String> styles = Arrays.stream(styleList.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .distinct()
                .collect(Collectors.toList());

        int combined = Font.NORMAL;

        if (styles.contains("BOLD")) combined |= Font.BOLD;
        if (styles.contains("ITALIC")) combined |= Font.ITALIC;
        if (styles.contains("UNDERLINE")) combined |= Font.UNDERLINE;
        if (styles.contains("STRIKETHRU") || styles.contains("STRIKETHROUGH")) combined |= Font.STRIKETHRU;

        return combined;
    }
}
