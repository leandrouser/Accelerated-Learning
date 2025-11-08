package com.estudo.demo.enums;

public enum TypePerson {
    ADMINISTRATOR("ROLE_ADMINISTRADOR"),
    USER("ROLE_USUARIO");

    private final String role;

    TypePerson(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
