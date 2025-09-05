package com.estudo.demo.controle;

import com.estudo.demo.DTOs.ProdutoRequestDTO;
import com.estudo.demo.DTOs.ProdutoResponseDTO;
import com.estudo.demo.Servico.ProdutoServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoControle {


    private final ProdutoServico produtoServico;

    public ProdutoControle(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@RequestBody ProdutoRequestDTO requestDTO){
        ProdutoResponseDTO produtoResponseDTO = produtoServico.criarProduto(requestDTO);
        return ResponseEntity.ok(produtoResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequestDTO requestDTO){

        ProdutoResponseDTO produtoResponseDTO = produtoServico.atualizarProduto(id, requestDTO);
        return ResponseEntity.ok(produtoResponseDTO);

    }

    @GetMapping("/search")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarProdutosPorTermo(
            @RequestParam("term") String term,
            @RequestParam(defaultValue = "0") int page,       // ← NOVO
            @RequestParam(defaultValue = "50") int size) {    // ← NOVO

        List<ProdutoResponseDTO> produtos = produtoServico.buscarProdutosPorTermo(term, page, size);
        return ResponseEntity.ok(produtos);
    }
}
