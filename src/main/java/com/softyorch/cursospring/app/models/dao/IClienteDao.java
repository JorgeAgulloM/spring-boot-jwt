package com.softyorch.cursospring.app.models.dao;

import com.softyorch.cursospring.app.models.entity.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>, CrudRepository<Cliente, Long> {
    @Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
    Cliente fetchByIdWithFacturas(Long id);
}
