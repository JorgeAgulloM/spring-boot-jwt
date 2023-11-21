package com.softyorch.cursospring.app.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.softyorch.cursospring.app.models.entity.Factura;
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
        table.addCell("Datos del cliente");
        table.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        table.addCell(factura.getCliente().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.addCell("Datos de la factura");
        table2.addCell("Folio: " + factura.getId());
        table2.addCell("Descripción: " + factura.getDescripcion());
        table2.addCell("Fecha: " + factura.getCreateAt());

        document.add(table);
        document.add(table2);
    }
}
