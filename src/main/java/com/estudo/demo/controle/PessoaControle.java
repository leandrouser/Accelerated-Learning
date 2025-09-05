package com.estudo.demo.controle;

import com.estudo.demo.DTOs.PessoaRequestDTO;
import com.estudo.demo.DTOs.PessoaResponseDTO;
import com.estudo.demo.Servico.PessoaServico;
import com.estudo.demo.repositorio.PessoaRepositorio;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pessoas")
public class PessoaControle {

    private final PessoaServico pessoaServico;
    private final PessoaRepositorio pessoaRepositorio;

    public PessoaControle(PessoaServico pessoaServico, PessoaRepositorio pessoaRepositorio) {
        this.pessoaServico = pessoaServico;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> criar(@Valid @RequestBody PessoaRequestDTO dto){
        PessoaResponseDTO pessoas = pessoaServico.criarPessoa(dto);
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> listar(){
        return ResponseEntity.ok(pessoaServico.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> buscar(@PathVariable Long id){
        return ResponseEntity.ok(pessoaServico.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaRequestDTO dto){
        return ResponseEntity.ok(pessoaServico.atualizar(id,dto));
    }
}
