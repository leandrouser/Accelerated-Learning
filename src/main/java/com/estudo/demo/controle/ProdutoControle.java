package com.estudo.demo.controle;

import com.estudo.demo.DTOs.requestDTO.ProdutoRequestDTO;
import com.estudo.demo.DTOs.response.ProdutoResponseDTO;
import com.estudo.demo.Servico.ProdutoServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoControle {

    private final ProdutoServico produtoServico;

    public ProdutoControle(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO salvo = produtoServico.criarProduto(dto);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable Long id,
            @RequestBody ProdutoRequestDTO requestDTO) {
        ProdutoResponseDTO atualizado = produtoServico.atualizarProduto(id, requestDTO);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarProdutosPorTermo(
            @RequestParam("term") String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        List<ProdutoResponseDTO> produtos = produtoServico.buscarProdutosPorTermo(term, page, size);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarProdutos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        List<ProdutoResponseDTO> produtos = produtoServico.listarTodosProdutos(page, size);
        return ResponseEntity.ok(produtos);
    }
}
