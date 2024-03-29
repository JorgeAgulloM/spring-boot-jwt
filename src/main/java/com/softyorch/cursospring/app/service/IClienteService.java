package com.softyorch.cursospring.app.service;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.models.entity.Factura;
import com.softyorch.cursospring.app.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    List<Cliente> findAll();

    Cliente findById(Long id);

    Page<Cliente> findAll(Pageable page);

    Cliente findOne(Long id);

    Cliente fetchClienteByIdWithFacturas(Long id);

    Cliente save(Cliente cliente);

    void delete(Long id);

    List<Producto> findByNombre(String term);

    void saveFactura(Factura factura);

    Producto findProductoById(Long id);

    Factura findFacturaById(Long id);

    void deleteFactura(Long id);

    Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id);
}
