package com.softyorch.cursospring.app.controllers;

import com.softyorch.cursospring.app.models.dao.IClienteDao;
import com.softyorch.cursospring.app.models.entity.Cliente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class ClienteController {

    @Autowired
    @Qualifier("ClienteDaoJPA")
    private IClienteDao clienteDao;

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteDao.findeAll());
        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {

        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de cliente");
        return "form";
    }

    /* en caso de que se llame la método con errores del formulario, este volverá a lanzar el formulario con los valores
    // que tenía insertados por el usuario, siempre y cuando la clase de la Entity entity se llame igual que el nombre
    // que estamos pasando al formulario, en caso de que no sea así, se puede validar con @ModelAttribute("cliente")
    */
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid  Cliente cliente, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }
        clienteDao.save(cliente);
        return "redirect:listar";
    }

}
