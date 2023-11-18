package com.softyorch.cursospring.app.controllers;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.models.entity.Factura;
import com.softyorch.cursospring.app.models.entity.Producto;
import com.softyorch.cursospring.app.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/factura")
@SessionAttributes("factura")
public class FacturaController {


    @Autowired
    private IClienteService clienteService;

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

}
