package com.softyorch.cursospring.app.models.dao;


import com.softyorch.cursospring.app.models.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
