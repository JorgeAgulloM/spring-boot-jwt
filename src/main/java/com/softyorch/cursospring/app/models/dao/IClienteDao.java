package com.softyorch.cursospring.app.models.dao;

import java.util.List;

import com.softyorch.cursospring.app.models.entity.Cliente;

public interface IClienteDao {

	public List<Cliente> findeAll();

	public Cliente findOne(Long id);

	public void save(Cliente cliente);

	public void delete(Long id);
}
