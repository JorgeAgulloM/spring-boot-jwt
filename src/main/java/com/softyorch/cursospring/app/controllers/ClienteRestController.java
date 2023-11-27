package com.softyorch.cursospring.app.controllers;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.service.IClienteService;
import com.softyorch.cursospring.app.view.xml.ClienteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping(value = "/listar")
    public ClienteList listar() {
        return new ClienteList(clienteService.findAll());
    }

    @GetMapping(value = "/clients")
    public List<Cliente> clients() {
        return clienteService.findAll();
    }

    @GetMapping(value = "/clients/{id}")
    public Cliente show(@PathVariable Long id) {
        return clienteService.findById(id);
    }

    @PostMapping(value = "/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@RequestBody Cliente client) {
        return clienteService.save(client);
    }

    @PutMapping(value = "/clients/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente update(@RequestBody Cliente client, @PathVariable Long id) {
        System.out.println("################  Client: " + client);
        System.out.println("################  Id: " + id);


        Cliente currentClient = clienteService.findById(id);

        currentClient.setName(client.getName());
        currentClient.setSurname(client.getSurname());
        currentClient.setEmail(client.getEmail());

        return clienteService.save(currentClient);
    }

    @DeleteMapping(value = "/clients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Cliente currentClient = clienteService.findById(id);
        clienteService.delete(currentClient.getId());
    }

}
