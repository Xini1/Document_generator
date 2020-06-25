package by.aistexped.documentgenerator;

import org.apache.poi.xwpf.usermodel.*;

import java.util.Map;

public class Replacer {

    private XWPFDocument document;

    private Logger logger = Logger.getInstance();

    public Replacer(XWPFDocument document, Map<Property, String> properties) {
        logger.logConstructorInvocation(getClass(), document.getClass().getSimpleName(), properties.toString());

        this.document = document;
    }

    public void replace(Replacements replacements) {
        logger.logMethodInvocation(getClass(), replacements.toString());

        for (XWPFHeader header : document.getHeaderList()) {
            for (XWPFParagraph paragraph : header.getParagraphs()) {
                replacements.apply(paragraph);
            }
        }

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replacements.apply(paragraph);
        }

        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replacements.apply(paragraph);
                    }
                }
            }
        }
    }
}
