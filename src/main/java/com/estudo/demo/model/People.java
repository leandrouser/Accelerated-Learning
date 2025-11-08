package com.estudo.demo.model;

import com.estudo.demo.enums.TypePerson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "pessoas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @CPF
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TypePerson type;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private boolean active = true;

}
