package com.softyorch.cursospring.app.service;

import com.softyorch.cursospring.app.models.dao.IClienteDao;
import com.softyorch.cursospring.app.models.entity.Cliente;
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

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {return (List<Cliente>) clienteDao.findAll();}

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable page) {
        return clienteDao.findAll(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {return clienteDao.findById(id).orElse(null);}

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);
    }
}
