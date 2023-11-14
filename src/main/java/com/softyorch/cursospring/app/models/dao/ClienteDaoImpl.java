package com.softyorch.cursospring.app.models.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.softyorch.cursospring.app.models.entity.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository("ClienteDaoJPA")
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional()
	@Override
	public List<Cliente> findeAll() {
		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional()
	public void save(Cliente cliente) {
		em.persist(cliente);
	}

}
