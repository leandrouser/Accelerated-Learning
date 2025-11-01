package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.BordadoRequestDTO;
import com.estudo.demo.DTOs.response.BordadoResponseDTO;
import com.estudo.demo.model.Bordados;
import com.estudo.demo.model.Cliente;
import com.estudo.demo.repositorio.BordadoRepositorio;
import com.estudo.demo.repositorio.ClienteRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BordadoServico {

    private final BordadoRepositorio bordadoRepositorio;
    private final ClienteRepositorio clienteRepositorio;

    public BordadoServico(BordadoRepositorio bordadoRepositorio, ClienteRepositorio clienteRepositorio) {
        this.bordadoRepositorio = bordadoRepositorio;
        this.clienteRepositorio = clienteRepositorio;
    }

    @Transactional
    public BordadoResponseDTO salvarOuAtualizarComArquivo(Long id, BordadoRequestDTO dto, MultipartFile file) {
        // --- Busca cliente ---
        Cliente cliente = clienteRepositorio.findById(dto.getCliente_id())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.getCliente_id()));

        Bordados bordado;

        if (id != null) {
            // Atualização
            bordado = bordadoRepositorio.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Bordado não encontrado com ID: " + id));
        } else {
            // Novo registro
            bordado = new Bordados();
            bordado.setDataCadastro(LocalDate.now());
        }

        // --- Atribuição dos campos básicos ---
        bordado.setCliente(cliente);
        bordado.setDataEntrega(dto.getDataEntrega());
        bordado.setValor(dto.getValor() != null ? dto.getValor() : BigDecimal.ZERO);
        bordado.setDescricao(dto.getDescricao());

        // --- Lógica do arquivo ---
        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytesArquivo = file.getBytes();
                String nomeArquivo = file.getOriginalFilename();

                bordado.setArquivo(bytesArquivo);
                bordado.setNomeArquivo(nomeArquivo);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar upload do arquivo do bordado", e);
            }
        }

        // --- Persistência ---
        Bordados salvo = bordadoRepositorio.save(bordado);
        return toResponseDTO(salvo);
    }

    public List<BordadoResponseDTO> listarBordados() {
        return bordadoRepositorio.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public BordadoResponseDTO buscarPorId(Long id) {
        Bordados bordado = bordadoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bordado não encontrado com ID: " + id));
        return toResponseDTO(bordado);
    }

    public byte[] obterArquivo(Long id) {
        Bordados bordado = bordadoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bordado não encontrado com ID: " + id));

        if (bordado.getArquivo() == null) {
            throw new EntityNotFoundException("Este bordado não possui arquivo associado.");
        }

        return bordado.getArquivo();
    }

    private BordadoResponseDTO toResponseDTO(Bordados bordado) {
        BordadoResponseDTO dto = new BordadoResponseDTO();
        dto.setId(bordado.getId());
        dto.setDataCadastro(bordado.getDataCadastro());
        dto.setDataEntrega(bordado.getDataEntrega());
        dto.setValor(bordado.getValor());
        dto.setDescricao(bordado.getDescricao());
        dto.setNomeArquivo(bordado.getNomeArquivo());
        dto.setClienteNome(bordado.getCliente() != null ? bordado.getCliente().getUsername() : "Cliente não associado");

        return dto;
    }
}
