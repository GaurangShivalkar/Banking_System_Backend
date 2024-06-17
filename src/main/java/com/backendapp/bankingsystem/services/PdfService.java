package com.backendapp.bankingsystem.services;


import com.backendapp.bankingsystem.models.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfService {

    public byte[] generateTransactionStatusPdf(List<Transaction> transactions) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            setRectanglePdf(document);

            Paragraph chunk = new Paragraph("Transaction Status Report", getFont("Header"));
            chunk.setAlignment(Element.ALIGN_CENTER);
            document.add(chunk);

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            addTableHeader(table);

            for (Transaction transaction : transactions) {
                addRows(table, transaction);
            }

            document.add(table);
            document.close();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addRows(PdfPTable table, Transaction transaction) {
        table.addCell(String.valueOf(transaction.getTransactionId()));
        table.addCell(transaction.getSourceAccountId());
        table.addCell(transaction.getDestinationAccountId());
        table.addCell(String.valueOf(transaction.getAmount()));
        table.addCell(transaction.getTransactionStatus());
        table.addCell(transaction.getTransactionMethod());
        table.addCell(transaction.getTransactionType());
        table.addCell(transaction.getTimestamp().toString());
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Transaction ID", "Source Account", "Destination Account", "Amount", "Status", "Method", "Type", "Timestamp")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    //header.setBackgroundColor(BaseColor.YELLOW);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type) {
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void setRectanglePdf(Document document) {
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderWidth(1);
        try {
            document.add(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
