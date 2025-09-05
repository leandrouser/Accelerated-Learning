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
    public BordadoResponseDTO criarBordado(BordadoRequestDTO dto) {
        Pessoas cliente = pessoaRepositorio.findById(dto.getCliente_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if (cliente.getTipo() != TipoPessoa.CLIENTE) {
            throw new IllegalArgumentException("A pessoa selecionada deve ser do tipo CLIENTE");
        }

        Bordados bordados = new Bordados();
        bordados.setCliente(cliente);
        bordados.setDataCadastro(LocalDate.now());
        bordados.setDataEntrega(dto.getDataEntrega());
        bordados.setValor(dto.getValor() != null ? dto.getValor() : BigDecimal.ZERO);
        bordados.setDescricao(dto.getDescricao());
        bordados.setArquivo(dto.getArquivo());
        bordados.setNomeArquivo(dto.getNomeArquivo());

        Bordados salvo = bordadoRepositorio.save(bordados);
        return toResponseDTO(salvo);
    }

    @Transactional
    public BordadoResponseDTO atualizarBordado(Long id, BordadoRequestDTO dto) {
        Bordados bordados = bordadoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bodado não encontrado"));

        Pessoas pessoas = pessoaRepositorio.findById(dto.getCliente_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if (pessoas.getTipo() != TipoPessoa.CLIENTE){
            throw new IllegalArgumentException("A pessoa seclecionada deve ser do tipo CLIENTE");

        }

        bordados.setCliente(pessoas);
        bordados.setDataEntrega(dto.getDataEntrega());
        bordados.setValor(dto.getValor());
        bordados.setDescricao(dto.getDescricao());

        Bordados atualizado = bordadoRepositorio.save(bordados);
        return toResponseDTO(atualizado);
    }

        public List<BordadoResponseDTO> listarBordados(){
            return bordadoRepositorio.findAll()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());


        }

    public BordadoResponseDTO buscarPorId(Long id){
        Bordados bordado = bordadoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bordado não encontrado"));
        return toResponseDTO(bordado);
    }

    public Bordados buscarEntidadePorId(Long id) {
        return bordadoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bordado não encontrado"));
    }

    @Transactional
    public BordadoResponseDTO salvarComArquivo(BordadoRequestDTO dto, MultipartFile file) {
        Pessoas cliente = pessoaRepositorio.findById(dto.getCliente_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if (cliente.getTipo() != TipoPessoa.CLIENTE) {
            throw new IllegalArgumentException("A pessoa selecionada deve ser do tipo CLIENTE");
        }

        Bordados bordados = new Bordados();
        bordados.setCliente(cliente);
        bordados.setDataCadastro(LocalDate.now());
        bordados.setDataEntrega(dto.getDataEntrega());
        bordados.setValor(dto.getValor() != null ? dto.getValor() : BigDecimal.ZERO);
        bordados.setDescricao(dto.getDescricao());

        try {
            if (file != null && !file.isEmpty()) {
                bordados.setArquivo(file.getBytes());
                bordados.setNomeArquivo(file.getOriginalFilename());
                bordados.setTipoArquivo(file.getContentType());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo", e);
        }

        Bordados salvo = bordadoRepositorio.save(bordados);
        return toResponseDTO(salvo);
    }


    private BordadoResponseDTO toResponseDTO(Bordados bordados) {
        BordadoResponseDTO dto = new BordadoResponseDTO();
        dto.setId(bordados.getId());
        dto.setDataCadastro(bordados.getDataCadastro());
        dto.setDataEntrega(bordados.getDataEntrega());
        dto.setValor(bordados.getValor());
        dto.setDescricao(bordados.getDescricao());
        dto.setClienteNome(bordados.getCliente().getNome());
        dto.setNomeArquivo(bordados.getNomeArquivo());
        return dto;
    }

}
