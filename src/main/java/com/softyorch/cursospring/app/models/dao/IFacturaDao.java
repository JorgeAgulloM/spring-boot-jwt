package com.softyorch.cursospring.app.models.dao;

import com.softyorch.cursospring.app.models.entity.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

}
