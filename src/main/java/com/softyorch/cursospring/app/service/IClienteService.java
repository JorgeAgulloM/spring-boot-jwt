package com.softyorch.cursospring.app.service;

import com.softyorch.cursospring.app.models.entity.Cliente;

import java.util.List;

public interface IClienteService {

    public List<Cliente> findeAll();

    public Cliente findOne(Long id);

    public void save(Cliente cliente);

    public void delete(Long id);
}
