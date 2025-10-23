package com.estudo.demo.enums;

public enum TipoPessoa {
    ADMINISTRADOR("ROLE_ADMINISTRADOR"),
    USUARIO("ROLE_USUARIO");

    private final String role;

    TipoPessoa(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
