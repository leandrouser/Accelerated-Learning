package com.estudo.demo.controle;

import com.estudo.demo.DTOs.requestDTO.CategoriaRequestDTO;
import com.estudo.demo.DTOs.response.CategoriaResponseDTO;
import com.estudo.demo.Servico.CategoriaServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
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
