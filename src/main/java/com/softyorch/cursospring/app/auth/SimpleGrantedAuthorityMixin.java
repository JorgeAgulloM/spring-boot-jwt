package com.softyorch.cursospring.app.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.softyorch.cursospring.app.auth.service.JWTServiceImpl.AUTHORITY;

public abstract class SimpleGrantedAuthorityMixin {
    @JsonCreator
    public SimpleGrantedAuthorityMixin(@JsonProperty(value = AUTHORITY) String role) {}
}
