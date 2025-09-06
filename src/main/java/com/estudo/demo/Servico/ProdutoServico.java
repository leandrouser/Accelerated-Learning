package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.ProdutoRequestDTO;
import com.estudo.demo.DTOs.ProdutoResponseDTO;
import com.estudo.demo.model.Categorias;
import com.estudo.demo.model.Produtos;
import com.estudo.demo.repositorio.CategoriaRepositorio;
import com.estudo.demo.repositorio.ProdutoRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoServico {

    private final ProdutoRepositorio produtoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;

    public ProdutoServico(ProdutoRepositorio produtoRepositorio,
                          CategoriaRepositorio categoriaRepositorio) {
        this.produtoRepositorio = produtoRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
    }

    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO produtoRequestDTO){
        Categorias categorias = categoriaRepositorio.findById(produtoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produtos produtos = new Produtos();
        produtos.setCodigoBarras(produtoRequestDTO.getCodigoBarras());
        produtos.setNome(produtoRequestDTO.getNome());
        produtos.setDescricao(produtoRequestDTO.getDescricao());
        produtos.setEstoque(produtoRequestDTO.getEstoque());
        produtos.setPreco(produtoRequestDTO.getPreco());
        produtos.setCategorias(categorias);

        produtoRepositorio.save(produtos);

        return toDTO(produtos);
    }

    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO produtoRequestDTO){
        Produtos produtos = produtoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Categorias categorias = categoriaRepositorio.findById(produtoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        produtos.setNome(produtoRequestDTO.getNome());
        produtos.setCodigoBarras(produtoRequestDTO.getCodigoBarras());
        produtos.setDescricao(produtoRequestDTO.getDescricao());
        produtos.setEstoque(produtoRequestDTO.getEstoque());
        produtos.setPreco(produtoRequestDTO.getPreco());
                produtos.setCategorias(categorias);

        produtoRepositorio.save(produtos);

        return toDTO(produtos);
    }

    public List<ProdutoResponseDTO> buscarProdutosPorTermo(String termo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Produtos> produtos = produtoRepositorio.searchByMultipleFields(termo.toLowerCase(), pageable);

        return produtos.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> listarTodosProdutos(int page, int size) {
        return produtoRepositorio.findAll(PageRequest.of(page, size))
                .stream()
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(),
                        produto.getCodigoBarras(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getCategorias().getNome(),
                        produto.getEstoque()

                ))
                .toList();
    }

    private ProdutoResponseDTO toDTO(Produtos produtos) {
        return new ProdutoResponseDTO(
                produtos.getId(),
                produtos.getCodigoBarras(),
                produtos.getNome(),
                produtos.getDescricao(),
                produtos.getPreco(),
                produtos.getCategorias().getNome(),
                produtos.getEstoque()

        );
    }
}
