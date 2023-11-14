package com.softyorch.cursospring.app.models.dao;

import com.softyorch.cursospring.app.models.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface IClienteDao extends CrudRepository<Cliente, Long> {}
