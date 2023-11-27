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
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.awt.*;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Component(value = "factura/detail")
public class FacturaPdfView extends AbstractPdfView {

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

        MessageSourceAccessor messages = getMessageSourceAccessor();

        Factura factura = (Factura) model.get("factura");

        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);

        PdfPCell cell;
        cell = new PdfPCell(new Phrase(Objects.requireNonNull(messages).getMessage("text.factura.ver.datos.cliente")));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        table.addCell(cell);
        table.addCell(factura.getCliente().getName() + " " + factura.getCliente().getSurname());
        table.addCell(factura.getCliente().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messages.getMessage("text.factura.ver.datos.factura")));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);

        table2.addCell(cell);
        table2.addCell(messages.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
        table2.addCell(messages.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
        table2.addCell(messages.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidths(new float[]{3.5f, 1, 1, 1});

        table3.setSpacingAfter(20);
        table3.addCell(messages.getMessage("text.factura.form.item.nombre"));
        table3.addCell(messages.getMessage("text.factura.form.item.precio"));
        table3.addCell(messages.getMessage("text.factura.form.item.cantidad"));
        table3.addCell(messages.getMessage("text.factura.form.item.total"));

        for (ItemFatura item : factura.getItems()) {
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

        cell = new PdfPCell(new Phrase(messages.getMessage("text.factura.form.total") + ": "));
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
