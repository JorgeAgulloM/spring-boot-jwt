package com.softyorch.cursospring.app.models.dao;


import com.softyorch.cursospring.app.models.entity.SysUser;
import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<SysUser, Long> {

    SysUser findByUsername(String username);
}
