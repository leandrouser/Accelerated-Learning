package com.estudo.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categoria")
public class Categorias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String nome;

    @OneToMany (mappedBy = "categorias",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produtos> produtos = new ArrayList<>();
}
