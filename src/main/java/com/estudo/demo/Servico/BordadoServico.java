package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.BordadoRequestDTO;
import com.estudo.demo.DTOs.BordadoResponseDTO;
import com.estudo.demo.enums.TipoPessoa;
import com.estudo.demo.model.Bordados;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.BordadoRepositorio;
import com.estudo.demo.repositorio.PessoaRepositorio;
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
    private final PessoaRepositorio pessoaRepositorio;

    public BordadoServico(BordadoRepositorio bordadoRepositorio, PessoaRepositorio pessoaRepositorio) {
        this.bordadoRepositorio = bordadoRepositorio;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    @Transactional
    public BordadoResponseDTO salvarOuAtualizarComArquivo(Long id, BordadoRequestDTO dto, MultipartFile file) {
        Pessoas cliente = pessoaRepositorio.findById(dto.getCliente_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if (cliente.getTipo() != TipoPessoa.CLIENTE) {
            throw new IllegalArgumentException("A pessoa selecionada deve ser do tipo CLIENTE");
        }

        Bordados bordado;

        if (id != null) {
            // Atualização
            bordado = bordadoRepositorio.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Bordado não encontrado"));
            bordado.setCliente(cliente);
            bordado.setDataEntrega(dto.getDataEntrega());
            bordado.setValor(dto.getValor() != null ? dto.getValor() : BigDecimal.ZERO);
            bordado.setDescricao(dto.getDescricao());

            if (file != null && !file.isEmpty()) {
                try {
                    bordado.setArquivo(file.getBytes());
                    bordado.setNomeArquivo(file.getOriginalFilename());
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao processar arquivo", e);
                }
            }

        } else {
            // Criação
            bordado = new Bordados();
            bordado.setCliente(cliente);
            bordado.setDataCadastro(LocalDate.now());
            bordado.setDataEntrega(dto.getDataEntrega());
            bordado.setValor(dto.getValor() != null ? dto.getValor() : BigDecimal.ZERO);
            bordado.setDescricao(dto.getDescricao());

            if (file != null && !file.isEmpty()) {
                try {
                    bordado.setArquivo(file.getBytes());
                    bordado.setNomeArquivo(file.getOriginalFilename());
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao processar arquivo", e);
                }
            }
        }

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
                .orElseThrow(() -> new IllegalArgumentException("Bordado não encontrado"));
        return toResponseDTO(bordado);
    }

    @Transactional
    public BordadoResponseDTO atualizarComArquivo(Long id, BordadoRequestDTO dto, MultipartFile file) {
        Bordados bordados = bordadoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bordado não encontrado"));

        Pessoas cliente = pessoaRepositorio.findById(dto.getCliente_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if (cliente.getTipo() != TipoPessoa.CLIENTE) {
            throw new IllegalArgumentException("A pessoa selecionada deve ser do tipo CLIENTE");
        }

        bordados.setCliente(cliente);
        bordados.setDataEntrega(dto.getDataEntrega());
        bordados.setValor(dto.getValor());
        bordados.setDescricao(dto.getDescricao());

        try {
            if (file != null && !file.isEmpty()) {
                bordados.setArquivo(file.getBytes());
                bordados.setNomeArquivo(file.getOriginalFilename());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo", e);
        }

        Bordados atualizado = bordadoRepositorio.save(bordados);
        return toResponseDTO(atualizado);
    }


    private BordadoResponseDTO toResponseDTO(Bordados bordado) {
        BordadoResponseDTO dto = new BordadoResponseDTO();
        dto.setId(bordado.getId());
        dto.setDataCadastro(bordado.getDataCadastro());
        dto.setDataEntrega(bordado.getDataEntrega());
        dto.setValor(bordado.getValor());
        dto.setDescricao(bordado.getDescricao());
        dto.setClienteNome(bordado.getCliente().getNome());
        dto.setNomeArquivo(bordado.getNomeArquivo());
        return dto;
    }
}
