package com.estudo.demo.controle;

import com.estudo.demo.DTOs.requestDTO.ProdutoRequestDTO;
import com.estudo.demo.DTOs.response.ProdutoResponseDTO;
import com.estudo.demo.Servico.ProdutoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoControle {

    private final ProdutoServico produtoServico;
    private final PagedResourcesAssembler<ProdutoResponseDTO> pagedResourcesAssembler;

    @Autowired
    public ProdutoControle(ProdutoServico produtoServico, PagedResourcesAssembler<ProdutoResponseDTO> pagedResourcesAssembler) {
        this.produtoServico = produtoServico;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
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
    public ResponseEntity<PagedModel<EntityModel<ProdutoResponseDTO>>> buscarProdutosPorTermo(
            @RequestParam("term") String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProdutoResponseDTO> produtos = produtoServico.buscarProdutosPorTermo(term, page, size);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(produtos));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProdutoResponseDTO>>> listarProdutos(
            @RequestParam Optional<Integer> estoque,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Page<ProdutoResponseDTO> produtos;

        if (estoque.isPresent()) {
            produtos = produtoServico.listarProdutosSemEstoque(estoque.get(), page, size);
        } else {
            produtos = produtoServico.listarTodosProdutos(page, size);
        }

        PagedModel<EntityModel<ProdutoResponseDTO>> pagedModel = pagedResourcesAssembler.toModel(produtos);
        return ResponseEntity.ok(pagedModel);
    }


    @GetMapping("/out-of-stock")
    public ResponseEntity<Page<ProdutoResponseDTO>> listarProdutosSemEstoque(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProdutoResponseDTO> produtos = produtoServico.listarProdutosSemEstoque(0, page, size);
        return ResponseEntity.ok(produtos);
    }

}
