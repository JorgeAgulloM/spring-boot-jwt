package com.softyorch.cursospring.app.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthoritiesMixin {
    @JsonCreator
    public SimpleGrantedAuthoritiesMixin(@JsonProperty(value = "authority") String role) {}
}
