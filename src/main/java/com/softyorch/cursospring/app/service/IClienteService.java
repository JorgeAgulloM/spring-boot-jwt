package com.softyorch.cursospring.app.service;

import com.softyorch.cursospring.app.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable page);

    public Cliente findOne(Long id);

    public void save(Cliente cliente);

    public void delete(Long id);
}
