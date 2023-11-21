package com.softyorch.cursospring.app.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.softyorch.cursospring.app.models.entity.Factura;
import com.softyorch.cursospring.app.models.entity.ItemFatura;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Phaser;

@Component(value = "factura/detail")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messages;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildPdfDocument(
            Map<String, Object> model,
            Document document,
            PdfWriter writer,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        Locale locale = localeResolver.resolveLocale(request);

        Factura factura = (Factura) model.get("factura");

        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);

        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase(messages.getMessage("text.factura.ver.datos.cliente", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        table.addCell(cell);
        table.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        table.addCell(factura.getCliente().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messages.getMessage("text.factura.ver.datos.factura", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);

        table2.addCell(cell);
        table2.addCell("Folio: " + factura.getId());
        table2.addCell("Descripción: " + factura.getDescripcion());
        table2.addCell("Fecha: " + factura.getCreateAt());

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidths(new float [] {3.5f, 1, 1, 1});

        table3.setSpacingAfter(20);
        table3.addCell("Producto");
        table3.addCell("Precio");
        table3.addCell("Cantidad");
        table3.addCell("Total");

        for (ItemFatura item: factura.getItems()) {
            table3.addCell(item.getProducto().getNombre());
            cell = new PdfPCell(new Phrase(item.getProducto().getPrecio().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(item.calculateImport().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table3.addCell(cell);
        }

        cell = new PdfPCell(new Phrase("Total"));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell);
        cell = new PdfPCell(new Phrase(factura.getTotal().toString()));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell);

        document.add(table);
        document.add(table2);
        document.add(table3);

    }
}
