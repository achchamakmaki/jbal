package com.example.advcontrol.controller;

import com.example.advcontrol.entity.Order;
import com.example.advcontrol.entity.OrderItem;
import com.example.advcontrol.repository.OrderRepository;
import com.example.advcontrol.service.OrderService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.time.LocalDate;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    @GetMapping
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    @PutMapping("/{id}/validate")
    public Order validate(@PathVariable Long id) {
        return orderService.validate(id, "Manager");
    }

    @GetMapping("/{id}/delivery-note")
    public void generateDeliveryNote(
            @PathVariable Long id,
            HttpServletResponse response) throws Exception {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=BL_" + order.getReference() + ".pdf");

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.BLACK);
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 11);
        Font smallFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.GRAY);

// Header société
        Paragraph company = new Paragraph("JBEL NOUR INDUSTRIE", headerFont);
        company.setAlignment(Element.ALIGN_LEFT);
        document.add(company);

        Paragraph subtitle = new Paragraph("Gestion ADV - Pré-intégration Sage X3", smallFont);
        subtitle.setAlignment(Element.ALIGN_LEFT);
        document.add(subtitle);

        document.add(new Paragraph(" "));

// Titre
        Paragraph title = new Paragraph("BON DE LIVRAISON", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

// Infos commande
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10);
        infoTable.setWidths(new float[]{1, 1});

        infoTable.addCell(cell("Référence BL", headerFont));
        infoTable.addCell(cell(order.getReference(), normalFont));

        infoTable.addCell(cell("Date", headerFont));
        infoTable.addCell(cell(LocalDate.now().toString(), normalFont));

        infoTable.addCell(cell("Client", headerFont));
        infoTable.addCell(cell(order.getClientName(), normalFont));

        infoTable.addCell(cell("Ville", headerFont));
        infoTable.addCell(cell(order.getCity(), normalFont));

        infoTable.addCell(cell("Transport", headerFont));
        infoTable.addCell(cell(order.getTransportMode(), normalFont));

        infoTable.addCell(cell("Statut", headerFont));
        infoTable.addCell(cell(order.getStatus().toString(), normalFont));

        document.add(infoTable);

        document.add(new Paragraph(" "));

// Tableau articles
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 5, 2, 2});

        table.addCell(headerCell("Code"));
        table.addCell(headerCell("Produit"));
        table.addCell(headerCell("Quantité"));
        table.addCell(headerCell("Prix U."));

        for (OrderItem item : order.getItems()) {
            table.addCell(cell(item.getArticleCode(), normalFont));
            table.addCell(cell(item.getArticleName(), normalFont));
            table.addCell(cell(String.valueOf(item.getQuantity()), normalFont));
            table.addCell(cell(item.getUnitPrice() + " DH", normalFont));
        }

        document.add(table);

        document.add(new Paragraph(" "));

// Total
        Paragraph total = new Paragraph("Montant total : " + order.getTotalAmount() + " DH", headerFont);
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

// Signatures
        PdfPTable signatureTable = new PdfPTable(2);
        signatureTable.setWidthPercentage(100);
        signatureTable.setWidths(new float[]{1, 1});

        signatureTable.addCell(signatureCell("Signature Chauffeur"));
        signatureTable.addCell(signatureCell("Signature Client"));

        document.add(signatureTable);

        document.add(new Paragraph(" "));

        Paragraph footer = new Paragraph(
                "Document généré automatiquement par ADV Control",
                smallFont
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }

    private PdfPCell cell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        cell.setBorderColor(Color.LIGHT_GRAY);
        return cell;
    }

    private PdfPCell headerCell(String text) {
        Font font = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(52, 73, 94));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell signatureCell(String text) {
        Font font = new Font(Font.HELVETICA, 11, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text + "\n\n\n\n__________________", font));
        cell.setPadding(15);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(Color.LIGHT_GRAY);
        return cell;
    }


}
