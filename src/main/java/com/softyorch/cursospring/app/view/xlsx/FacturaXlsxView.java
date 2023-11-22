package com.softyorch.cursospring.app.view.xlsx;

import com.softyorch.cursospring.app.models.entity.Factura;
import com.softyorch.cursospring.app.models.entity.ItemFatura;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Component(value = "factura/detail.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildExcelDocument(
            Map<String, Object> model,
            Workbook workbook,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        Locale locale = localeResolver.resolveLocale(request);
        MessageSourceAccessor messages = getMessageSourceAccessor();

        Factura factura = (Factura) model.get("factura");
        response.setHeader("Content-Disposition", "attachmente; filename=\"Factura_view.xlsx\"");

        Sheet sheet = workbook.createSheet(
                String.format(Objects.requireNonNull(messages).getMessage("text.factura.ver.titulo").split(":")[0], "Spring")
        );

        sheet.createRow(0).createCell(0)
                .setCellValue(messages.getMessage("text.factura.ver.datos.cliente"));
        sheet.createRow(1).createCell(0)
                .setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        sheet.createRow(2).createCell(0)
                .setCellValue(factura.getCliente().getEmail());

        sheet.createRow(4).createCell(0)
                .setCellValue(messages.getMessage("text.factura.ver.datos.factura"));
        sheet.createRow(5).createCell(0)
                .setCellValue(messages.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
        sheet.createRow(6).createCell(0)
                .setCellValue(messages.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
        sheet.createRow(7).createCell(0)
                .setCellValue(messages.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);

        /* Header */
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue(messages.getMessage("text.factura.form.item.nombre"));
        header.createCell(1).setCellValue(messages.getMessage("text.factura.form.item.precio"));
        header.createCell(2).setCellValue(messages.getMessage("text.factura.form.item.cantidad"));
        header.createCell(3).setCellValue(messages.getMessage("text.factura.form.item.total"));
        header.getCell(0).setCellStyle(headerStyle);
        header.getCell(1).setCellStyle(headerStyle);
        header.getCell(2).setCellStyle(headerStyle);
        header.getCell(3).setCellStyle(headerStyle);

        /* Values */
        int rowNum = 10;
        for (ItemFatura item : factura.getItems()) {
            Row row = sheet.createRow(rowNum ++);
            Cell cell = row.createCell(0);
            cell.setCellValue(item.getProducto().getNombre());
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(1);
            cell.setCellValue(item.getProducto().getPrecio());
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(3);
            cell.setCellValue(item.calculateImport());
            cell.setCellStyle(bodyStyle);
        }

        /* Total */
        Row total = sheet.createRow(rowNum);
        Cell cell = total.createCell(2);
        cell.setCellValue(messages.getMessage("text.factura.form.total") + ": ");
        cell.setCellStyle(bodyStyle);

        cell = total.createCell(3);
        cell.setCellValue(factura.getTotal());
        cell.setCellStyle(bodyStyle);
    }
}
