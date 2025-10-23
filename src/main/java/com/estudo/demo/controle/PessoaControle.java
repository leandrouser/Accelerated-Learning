package com.estudo.demo.controle;

import com.estudo.demo.DTOs.requestDTO.PessoaRequestDTO;
import com.estudo.demo.DTOs.response.PessoaResponseDTO;
import com.estudo.demo.Servico.PessoaServico;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaControle {

    private final PessoaServico pessoaServico;

    public PessoaControle(PessoaServico pessoaServico) {
        this.pessoaServico = pessoaServico;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PessoaResponseDTO> criar(@Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO pessoa = pessoaServico.criarPessoa(dto);
        return ResponseEntity.ok(pessoa);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<PessoaResponseDTO>> listar() {
        return ResponseEntity.ok(pessoaServico.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or (hasRole('USUARIO') and @pessoaServico.buscarPorId(#id).id == authentication.principal.id)")
    public ResponseEntity<PessoaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaServico.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or (hasRole('USUARIO') and #id == authentication.principal.id)")
    public ResponseEntity<PessoaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaRequestDTO dto) {
        return ResponseEntity.ok(pessoaServico.atualizar(id, dto));
    }
}