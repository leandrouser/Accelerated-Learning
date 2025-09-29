package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.CategoriaRequestDTO;
import com.estudo.demo.DTOs.response.CategoriaResponseDTO;
import com.estudo.demo.model.Categorias;
import com.estudo.demo.repositorio.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServico {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    // NOVO MÉTODO PARA LISTAR TODAS AS CATEGORIAS
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepositorio.findAll()
                .stream()
                .map(this::toDto) // Reutiliza o método de conversão
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO categoriaRequestDTO){
        Categorias categorias = new Categorias();
        categorias.setNome(categoriaRequestDTO.getNome());
        categoriaRepositorio.save(categorias);
        return toDto(categorias);
    }

    public CategoriaResponseDTO atualizarCategoria(Long id, CategoriaRequestDTO categoriaRequestDTO){
        Categorias categorias = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));
        categorias.setNome(categoriaRequestDTO.getNome());
        categoriaRepositorio.save(categorias);
        return toDto(categorias);
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        Categorias categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));
        return toDto(categoria);
    }

    // MÉTODO toDto CORRIGIDO PARA INCLUIR O ID
    private CategoriaResponseDTO toDto(Categorias categorias){
        // Agora retorna um DTO com ID e Nome, como definido no record
        return new CategoriaResponseDTO(categorias.getId(), categorias.getNome());
    }
}