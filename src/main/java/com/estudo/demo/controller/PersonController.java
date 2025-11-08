package com.estudo.demo.controller;

import com.estudo.demo.DTOs.requestDTO.PersonRequestDTO;
import com.estudo.demo.DTOs.response.PersonResponseDTO;
import com.estudo.demo.Servico.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PersonResponseDTO> createPerson(@Valid @RequestBody PersonRequestDTO dto) {
        PersonResponseDTO pessoa = personService.createPerson(dto);
        return ResponseEntity.ok(pessoa);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<PersonResponseDTO>> listar() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or (hasRole('USUARIO') and @pessoaServico.buscarPorId(#id).id == authentication.principal.id)")
    public ResponseEntity<PersonResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(personService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or (hasRole('USUARIO') and #id == authentication.principal.id)")
    public ResponseEntity<PersonResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PersonRequestDTO dto) {
        return ResponseEntity.ok(personService.atualizar(id, dto));
    }
}
