package com.softyorch.cursospring.app.models.entity;


import jakarta.persistence.*;

import static com.softyorch.cursospring.app.auth.service.JWTServiceImpl.AUTHORITIES;
import static com.softyorch.cursospring.app.auth.service.JWTServiceImpl.AUTHORITY;

@Entity
@Table(name = AUTHORITIES, uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", AUTHORITY})})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    private static final long serialVersionUID = 1L;
}
