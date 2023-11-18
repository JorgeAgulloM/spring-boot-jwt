package com.softyorch.cursospring.app.service;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.models.entity.Factura;
import com.softyorch.cursospring.app.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    List<Cliente> findAll();

    Page<Cliente> findAll(Pageable page);

    Cliente findOne(Long id);

    void save(Cliente cliente);

    void delete(Long id);

    List<Producto> findByNombre(String term);

    void saveFactura(Factura factura);
}
