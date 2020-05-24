package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import model.Book;
import model.Customer;
import model.Order;
import model.OrderContent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.stream.Stream;

public class PdfGenerator {
    private static Font fontTitleName = FontFactory.getFont(FontFactory.COURIER_BOLD, 24, BaseColor.BLACK);
    private static Font fontTitle = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
    private static Font fontParagraph = FontFactory.getFont(FontFactory.COURIER, 8, BaseColor.BLACK);
    private static LanguageResource lr = LanguageResource.getInstance();

    public static void generateOrderPDF(Order order) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        String fileName = String.format("%s%s.pdf",lr.getResources().getString("order_save_pdf"), order.getID());
        PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        // Paragraph for order contents and ID
        Paragraph titleParagraph = new Paragraph();
        titleParagraph.setFont(fontParagraph);
        Chunk chunk = new Chunk(lr.getResources().getString("bookstore_pdf"), fontTitleName);
        titleParagraph.add(chunk);
        newLine(titleParagraph, 2);
        chunk = new Chunk(String.format("%s %d, %s: %s", lr.getResources().getString("order_pdf"),
                order.getID(), lr.getResources().getString("date_pdf"), order.getDate().toString()), fontTitle);
        titleParagraph.add(chunk);
        newLine(titleParagraph);

        // Customer info
        Paragraph customerParagraph = new Paragraph();
        customerParagraph.setFont(fontParagraph);
        Customer c = order.getCustomer();
        String customerText = String.format("%s %s\n%s\n%s\n%s, %s",
                c.getFirstName(), c.getLastName(), c.getMail(), c.getAddress(), c.getCity(), c.getZip());
        chunk = new Chunk(customerText, fontParagraph);
        customerParagraph.add(chunk);

        // Table with order contents
        Paragraph orderParagraph = new Paragraph();
        orderParagraph.setFont(fontParagraph);
        newLine(orderParagraph);
        PdfPTable orderContentsTable = new PdfPTable(5);
        orderContentsTable.setWidths(new int[] {15, 45, 10, 15, 15});
        addTableHeader(orderContentsTable);
        double totalPrice = addRows(orderContentsTable, order.getOrderContents());
        orderParagraph.add(orderContentsTable);


        // Total price
        Paragraph totalParagraph = new Paragraph();
        totalParagraph.setFont(fontParagraph);
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        chunk = new Chunk(String.format("%s = %.2f\n" +
                "%s 10%c = %.2f\n" +
                "%s = %.2f", lr.getResources().getString("subtotal_pdf"), totalPrice,
                lr.getResources().getString("tax_pdf"),'%', totalPrice * 0.10,
                lr.getResources().getString("total_pdf"), totalPrice + totalPrice * 0.10));
        totalParagraph.add(chunk);

        document.add(titleParagraph);
        document.add(customerParagraph);
        document.add(orderParagraph);
        document.add(totalParagraph);
        document.close();
    }

    private static void addTableHeader(PdfPTable table) {
        Stream.of(lr.getResources().getString("book_id_pdf"), lr.getResources().getString("book_name_pdf"),
                lr.getResources().getString("qty_pdf"), lr.getResources().getString("unit_price_pdf"),
                lr.getResources().getString("amount_pdf"))
                .forEach(columnTitle ->{
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle, fontParagraph));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private static double addRows(PdfPTable table, ObservableList<OrderContent> orderContents) {
        double totalPrice = 0.0;

        for(OrderContent item : orderContents){
            Book book = item.getBook();
            double price = book.getPrice();
            int quantity = item.getQuantity();
            PdfPCell cell = new PdfPCell();

            // Book ID column
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPhrase(new Phrase(String.valueOf(book.getID()), fontParagraph));
            table.addCell(cell);

            // Book title column
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPhrase(new Phrase(book.getTitle(), fontParagraph));
            table.addCell(cell);

            // Book quantity column
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPhrase(new Phrase(String.valueOf(quantity), fontParagraph));
            table.addCell(cell);

            // Unit price column
            cell.setPhrase(new Phrase(String.valueOf(price), fontParagraph));
            table.addCell(cell);

            // Amount price column
            double total = price * quantity;
            totalPrice += total;
            cell.setPhrase(new Phrase(String.format("%.2f", total), fontParagraph));
            table.addCell(cell);
        }
        return totalPrice;
    }

    private static void newLine(Paragraph paragraph, int count) {
        while(count-- != 0)
            paragraph.add(Chunk.NEWLINE);
    }

    private static void newLine(Paragraph paragraph) {
        paragraph.add(Chunk.NEWLINE);
    }
}
