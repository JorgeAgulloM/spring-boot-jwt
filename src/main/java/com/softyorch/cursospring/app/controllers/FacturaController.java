package com.softyorch.cursospring.app.controllers;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.models.entity.Factura;
import com.softyorch.cursospring.app.models.entity.ItemFatura;
import com.softyorch.cursospring.app.models.entity.Producto;
import com.softyorch.cursospring.app.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/factura")
@SessionAttributes("factura")
public class FacturaController {


    @Autowired
    private IClienteService clienteService;

    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable(value = "id") Long id,
            Model model,
            RedirectAttributes flash
    ) {

        Factura factura = clienteService.findFacturaById(id);

        if (factura == null) {
            flash.addFlashAttribute("error", "La factura no exite en la base de datos");
            return "redirect:/listar";
        }

        model.addAttribute("factura", factura);
        model.addAttribute("title", "Factura: ".concat(factura.getDescripcion()));

        return "factura/detail";
    }

    @GetMapping("/form/{cliente_id}")
    public String create(
            @PathVariable(value = "cliente_id") Long clienteId,
            Map<String, Object> model,
            RedirectAttributes flash
    ) {

        Cliente cliente = clienteService.findOne(clienteId);

        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.put("factura", factura);
        model.put("title", "Crear Factura");

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
            SessionStatus status
    ) {

        if (result.hasErrors()) {
            model.addAttribute("title", "Crear Factura");
            return "factura/form";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("title", "Crear Factura");
            model.addAttribute("error", "Error: La factura NO puede no tener lineas!");
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
        flash.addFlashAttribute("success", "Factura creada con exito!");

        return "redirect:/detail/" + factura.getCliente().getId();
    }

}
