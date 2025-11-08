package com.estudo.demo.service;

import com.estudo.demo.DTOs.requestDTO.LoginRequestDTO;
import com.estudo.demo.DTOs.response.LoginResponseDTO;
import com.estudo.demo.enums.TypePerson;
import com.estudo.demo.model.People;

import com.estudo.demo.repository.PersonRepository;
import com.estudo.demo.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PersonRepository personRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PersonRepository personRepository,
                                 JwtTokenProvider jwtTokenProvider,
                                 PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        People pessoa = personRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com este CPF."));

        if (!pessoa.isActive()) {
            throw new RuntimeException("Usuário inativo.");
        }

        if (pessoa.getType() != TypePerson.ADMINISTRATOR && pessoa.getType() != TypePerson.USER) {
            throw new RuntimeException("Tipo de usuário não autorizado para login.");
        }

        if (!passwordEncoder.matches(request.password(), pessoa.getPassword())) {
            throw new RuntimeException("Senha incorreta.");
        }

        String token = jwtTokenProvider.generateToken(pessoa.getCpf(), pessoa.getType().getRole());

        return new LoginResponseDTO(token, "Bearer", pessoa.getName(), pessoa.getCpf());
    }
}

