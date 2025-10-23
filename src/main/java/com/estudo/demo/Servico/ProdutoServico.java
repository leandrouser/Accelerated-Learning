package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.ProdutoRequestDTO;
import com.estudo.demo.DTOs.response.ProdutoResponseDTO;
import com.estudo.demo.model.Categorias;
import com.estudo.demo.model.Produtos;
import com.estudo.demo.repositorio.CategoriaRepositorio;
import com.estudo.demo.repositorio.ProdutoRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoServico {

    private final ProdutoRepositorio produtoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;

    public ProdutoServico(ProdutoRepositorio produtoRepositorio,
                          CategoriaRepositorio categoriaRepositorio) {
        this.produtoRepositorio = produtoRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
    }

    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO produtoRequestDTO) {
        Categorias categorias = categoriaRepositorio.findById(produtoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produtos produtos = new Produtos();
        produtos.setCodigoBarras(produtoRequestDTO.getCodigoBarras());
        produtos.setNome(produtoRequestDTO.getNome());
        produtos.setDescricao(produtoRequestDTO.getDescricao());
        produtos.setEstoque(produtoRequestDTO.getEstoque());
        produtos.setPreco(produtoRequestDTO.getPreco());
        produtos.setCategoria(categorias);

        produtoRepositorio.save(produtos);

        return converterParaDTO(produtos);
    }

    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produtos produtos = produtoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Categorias categorias = categoriaRepositorio.findById(produtoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        produtos.setNome(produtoRequestDTO.getNome());
        produtos.setCodigoBarras(produtoRequestDTO.getCodigoBarras());
        produtos.setDescricao(produtoRequestDTO.getDescricao());
        produtos.setEstoque(produtoRequestDTO.getEstoque());
        produtos.setPreco(produtoRequestDTO.getPreco());
        produtos.setCategoria(categorias);

        produtoRepositorio.save(produtos);

        return converterParaDTO(produtos);
    }

    public Page<ProdutoResponseDTO> buscarProdutosPorTermo(String termo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Produtos> produtosPage;

        try {
            Long idTermo = Long.parseLong(termo);
            produtosPage = produtoRepositorio.findByIdOrTextSearch(idTermo, termo, pageable);
        } catch (NumberFormatException e) {
            produtosPage = produtoRepositorio.findByTextSearch(termo, pageable);
        }

        return produtosPage.map(this::converterParaDTO);
    }

    public Page<ProdutoResponseDTO> listarTodosProdutos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Produtos> produtosPage = produtoRepositorio.findAll(pageable);

        return produtosPage.map(this::converterParaDTO);
    }

    public Page<ProdutoResponseDTO> listarProdutosSemEstoque(int estoque, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Produtos> produtosPage = produtoRepositorio.findByEstoqueLessThanEqualComCategoria(estoque, pageable);

        return produtosPage.map(this::converterParaDTO);
    }

    private ProdutoResponseDTO converterParaDTO(Produtos produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getCodigoBarras(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria() != null ? produto.getCategoria().getNome() : null,
                produto.getEstoque()
        );
    }
}