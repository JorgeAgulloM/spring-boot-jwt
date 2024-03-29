package com.softyorch.cursospring.app.controllers;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.service.IClienteService;
import com.softyorch.cursospring.app.view.xml.ClienteList;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> show(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Cliente client;

        try {
            client = clienteService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error in database query.");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (client == null) {
            response.put("message", "The client does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping(value = "/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody Cliente client, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Cliente newClient;

        if (validations(result, response))
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        try {
            newClient = clienteService.save(client);
        } catch (DataAccessException e) {
            response.put("message", "Error when user creating");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "Client created successful");
        response.put("client", newClient);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/clients/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente client, BindingResult result, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (validations(result, response))
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        Cliente currentClient = clienteService.findById(id);

        if (currentClient == null) {
            response.put("message", "Error in database query. The id client " + id + " does exist in database.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        currentClient.setName(client.getName());
        currentClient.setSurname(client.getSurname());
        currentClient.setEmail(client.getEmail());

        try {
            clienteService.save(currentClient);
        } catch (DataAccessException e) {
            response.put("message", "Error when user updating");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "Client updated successful");
        response.put("client", currentClient);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/clients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Cliente currentClient = clienteService.findById(id);

        try {
            clienteService.delete(currentClient.getId());
            response.put("message", "The id user " + id + " has been deleted.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("message", "Error when user deleting");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validations(BindingResult result, Map<String, Object> response) {
        if (result.hasErrors()) {
            //Forma básica de obtener los errores.
            /*List<String> errors = new ArrayList<>();

            for (FieldError error: result.getFieldErrors()) {
                errors.add("Field '" + error.getField() + "' " + error.getDefaultMessage());
            }*/
            List<String> errors = result
                    .getFieldErrors()
                    .stream()
                    .map( error -> "Field '" + error.getField() + "' " + error.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return true;
        }
        return false;
    }

}
