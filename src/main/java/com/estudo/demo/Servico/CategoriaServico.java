package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.CategoriaRequestDTO;
import com.estudo.demo.DTOs.response.CategoriaResponseDTO;
import com.estudo.demo.model.Categorias;
import com.estudo.demo.repositorio.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServico {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO categoriaRequestDTO){
        Categorias categorias = new Categorias();
        categorias.setNome(categoriaRequestDTO.getNome());
        categoriaRepositorio.save(categorias);
        return new CategoriaResponseDTO();
    }

    public CategoriaResponseDTO atualizarCategoria(CategoriaRequestDTO categoriaRequestDTO){
        Categorias categorias = new Categorias();
        categorias.setNome(categoriaRequestDTO.getNome());
        categoriaRepositorio.save(categorias);
        return new CategoriaResponseDTO();
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        Categorias categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));

        return toDto(categoria);
    }

    private CategoriaResponseDTO toDto(Categorias categorias){
        return new CategoriaResponseDTO(

                categorias.getNome()

                        );
    }
}
