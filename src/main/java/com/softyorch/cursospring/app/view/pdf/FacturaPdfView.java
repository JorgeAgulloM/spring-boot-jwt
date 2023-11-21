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
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.swing.*;
import java.util.Map;

@Component(value = "factura/detail")
public class FacturaPdfView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(
            Map<String, Object> model,
            Document document,
            PdfWriter writer,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        Factura factura = (Factura) model.get("factura");

        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);
        table.addCell("Datos del cliente");
        table.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        table.addCell(factura.getCliente().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);
        table2.addCell("Datos de la factura");
        table2.addCell("Folio: " + factura.getId());
        table2.addCell("Descripción: " + factura.getDescripcion());
        table2.addCell("Fecha: " + factura.getCreateAt());

        PdfPTable table3 = new PdfPTable(4);
        table3.setSpacingAfter(20);
        table3.addCell("Producto");
        table3.addCell("Precio");
        table3.addCell("Cantidad");
        table3.addCell("Total");

        for (ItemFatura item: factura.getItems()) {
            table3.addCell(item.getProducto().getNombre());
            table3.addCell(item.getProducto().getPrecio().toString());
            table3.addCell(item.getCantidad().toString());
            table3.addCell(item.calculateImport().toString());
        }

        PdfPCell cell = new PdfPCell(new Phrase("Total"));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell);
        table3.addCell(factura.getTotal().toString());

        document.add(table);
        document.add(table2);
        document.add(table3);

    }
}
