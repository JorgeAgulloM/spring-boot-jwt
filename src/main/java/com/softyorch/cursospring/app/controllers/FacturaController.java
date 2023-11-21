package com.softyorch.cursospring.app.controllers;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.models.entity.Factura;
import com.softyorch.cursospring.app.models.entity.ItemFatura;
import com.softyorch.cursospring.app.models.entity.Producto;
import com.softyorch.cursospring.app.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping(value = "/factura")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private MessageSource messages;

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable(value = "id") Long id,
            Model model,
            RedirectAttributes flash,
            Locale locale
    ) {

        Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id); //.findFacturaById(id);

        if (factura == null) {
            flash.addFlashAttribute(
                    "error",
                    messages.getMessage("text.factura.flash.db.error", null, locale)
            );
            return "redirect:/listar";
        }

        model.addAttribute("factura", factura);
        model.addAttribute(
                "title",
                String.format(
                        messages.getMessage("text.factura.ver.titulo", null, locale),
                        factura.getDescripcion())
        );


        return "factura/detail";
    }

    @GetMapping("/form/{cliente_id}")
    public String create(
            @PathVariable(value = "cliente_id") Long clienteId,
            Map<String, Object> model,
            RedirectAttributes flash,
            Locale locale
    ) {

        Cliente cliente = clienteService.findOne(clienteId);

        if (cliente == null) {
            flash.addFlashAttribute(
                    "error",
                    messages.getMessage("text.cliente.flash.db.error", null, locale)
            );
            return "redirect:/listar";
        }

        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.put("factura", factura);
        model.put("title", messages.getMessage("text.factura.form.titulo", null, locale));

        return "factura/form";
    }

    /*ResponseBody evita retornar una vista de thymeleaf y repuebla el body de la respuesta con el resultado convertido a json */
    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByNombre(term);
    }

    @PostMapping("/form")
    public String guardar(
            @Valid Factura factura,
            BindingResult result,
            Model model,
            @RequestParam(name = "item_id[]", required = false) Long[] itemId,
            @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
            RedirectAttributes flash,
            SessionStatus status,
            Locale locale
    ) {

        if (result.hasErrors()) {
            model.addAttribute(
                    "title",
                    messages.getMessage("text.factura.form.titulo", null, locale)
            );
            return "factura/form";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("title", messages.getMessage("text.factura.form.titulo", null, locale));
            model.addAttribute("error", messages.getMessage("text.factura.flash.lineas.error", null, locale));
            return "factura/form";
        }

        for (int idx = 0; idx < itemId.length; idx++) {
            Producto producto = clienteService.findProductoById(itemId[idx]);

            ItemFatura linea = new ItemFatura();
            linea.setCantidad(cantidad[idx]);
            linea.setProducto(producto);

            factura.addItemFactura(linea);
        }

        clienteService.saveFactura(factura);

        status.setComplete();
        flash.addFlashAttribute("success", messages.getMessage("text.factura.flash.crear.success", null, locale));

        return "redirect:/detail/" + factura.getCliente().getId();
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {

        Factura factura = clienteService.findFacturaById(id);

        if (factura != null) {
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("success", messages.getMessage("text.factura.flash.eliminar.success", null, locale));
            return "redirect:/detail/" + factura.getCliente().getId();
        }
        flash.addFlashAttribute("error", messages.getMessage("text.factura.flash.db.error", null, locale));
        return "redirect:/listar";
    }

}
