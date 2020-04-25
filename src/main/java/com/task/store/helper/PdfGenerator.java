package com.task.store.helper;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.task.store.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public class PdfGenerator {

    private static Logger logger = LoggerFactory.getLogger(PdfGenerator.class);

    private static final String PRODUCTS = "Products";
    private static final String NO_PRODUCTS = "There are no products available";
    private static final String TOTAL_PRICE = "Total Price: ";

    private static final String EAN = "EAN";
    private static final String NAME = "Name";
    private static final String PRICE = "Price";

    private static final int FONT_SIZE = 14;
    private static final int COLUMN_COUNT = 3;
    private static final int PADDING = 4;
    private static final int BORDER = 2;

    public static ByteArrayInputStream productPdf(List<Product> products) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            if (products.isEmpty()) {
                addParagraph(document, NO_PRODUCTS, Element.ALIGN_CENTER);
            } else {
                addParagraph(document, PRODUCTS, Element.ALIGN_CENTER);
                PdfPTable table = new PdfPTable(COLUMN_COUNT);
                addHeader(table);
                products.forEach(product -> addProductRow(table, product));
                addParagraph(document, getTotalPriceText(products), Element.ALIGN_RIGHT);
                document.add(table);
            }
            document.close();
        } catch (DocumentException e) {
            logger.error(e.toString());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static String getTotalPriceText(List<Product> products) {
        return TOTAL_PRICE + getTotalPrice(products);
    }

    private static BigDecimal getTotalPrice(List<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private static void addParagraph(Document document, String text, int align) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.COURIER, FONT_SIZE, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(align);
        document.add(Chunk.NEWLINE);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
    }

    private static void addHeader(PdfPTable table) {
        Stream.of(EAN, NAME, PRICE)
                .forEach(headerTitle -> {
                    PdfPCell header = new PdfPCell();
                    Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setBorderWidth(BORDER);
                    header.setPhrase(new Phrase(headerTitle, headFont));
                    table.addCell(header);
                });
    }

    private static void addProductRow(PdfPTable table, Product product) {
        PdfPCell eanCell = new PdfPCell(new Phrase(product.getEan()));
        addProductCell(table, eanCell);

        PdfPCell nameCell = new PdfPCell(new Phrase(product.getName()));
        addProductCell(table, nameCell);

        PdfPCell priceCell = new PdfPCell(new Phrase(String.valueOf(product.getPrice())));
        addProductCell(table, priceCell);
    }

    private static void addProductCell(PdfPTable table, PdfPCell cell) {
        cell.setPaddingRight(PADDING);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
    }
}
