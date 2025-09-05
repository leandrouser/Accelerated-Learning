package com.estudo.demo.controle;

import com.estudo.demo.DTOs.CategoriaRequestDTO;
import com.estudo.demo.DTOs.CategoriaResponseDTO;
import com.estudo.demo.Servico.CategoriaServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categoria")
public class CategoriaControle {

    private final CategoriaServico categoriaServico;

    public CategoriaControle(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@RequestBody CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO categoriaResponseDTO = categoriaServico.criarCategoria(requestDTO);
        return ResponseEntity.ok(categoriaResponseDTO);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> listarCategoria(@PathVariable Long id) {
        CategoriaResponseDTO categoriaResponseDTO = categoriaServico.buscarPorId(id);
        return ResponseEntity.ok(categoriaResponseDTO);
    }
}
