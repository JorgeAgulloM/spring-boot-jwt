package com.softyorch.cursospring.app.service;

import com.softyorch.cursospring.app.models.dao.IClienteDao;
import com.softyorch.cursospring.app.models.dao.IFacturaDao;
import com.softyorch.cursospring.app.models.dao.IProductoDao;
import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.models.entity.Factura;
import com.softyorch.cursospring.app.models.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/* @Service: Basado en el patrón de diseño Facade o 'Business Service Facade'
 * Un único punto puede usar distintos accesos DAOs o repositorios.
 * Además evitamos usar los DAO directamente desde los controller.
 */

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Autowired
    private IProductoDao productoDao;

    @Autowired
    private IFacturaDao facturaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {return (List<Cliente>) clienteDao.findAll();}

    @Transactional(readOnly = true)
    @Override
    public Cliente findById(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable page) {
        return clienteDao.findAll(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {return clienteDao.findById(id).orElse(null);}

    @Override
    public Cliente fetchClienteByIdWithFacturas(Long id) {return clienteDao.fetchByIdWithFacturas(id);}

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByNombre(String term) {
        return productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
    }

    @Override
    @Transactional
    public void saveFactura(Factura factura) {
        facturaDao.save(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findProductoById(Long id) {
        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteFactura(Long id) {facturaDao.deleteById(id);}

    @Override
    @Transactional(readOnly = true)
    public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
        return facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
    }
}
