package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.BordadoRequestDTO;
import com.estudo.demo.DTOs.response.BordadoResponseDTO;
// Removido: import com.estudo.demo.enums.TipoPessoa;
import com.estudo.demo.model.Bordados;
import com.estudo.demo.model.Cliente; // Importe Cliente
// Removido: import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.BordadoRepositorio;
import com.estudo.demo.repositorio.ClienteRepositorio; // Importe ClienteRepositorio
// Removido: import com.estudo.demo.repositorio.PessoaRepositorio;
import jakarta.persistence.EntityNotFoundException; // Boa prática usar essa exceção
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
    private final ClienteRepositorio clienteRepositorio; // <-- CORRIGIDO: Injetar ClienteRepositorio

    // Construtor Corrigido
    public BordadoServico(BordadoRepositorio bordadoRepositorio, ClienteRepositorio clienteRepositorio) {
        this.bordadoRepositorio = bordadoRepositorio;
        this.clienteRepositorio = clienteRepositorio; // <-- CORRIGIDO
    }

    @Transactional
    public BordadoResponseDTO salvarOuAtualizarComArquivo(Long id, BordadoRequestDTO dto, MultipartFile file) {
        // --- CORRIGIDO: Buscar a entidade Cliente ---
        Cliente cliente = clienteRepositorio.findById(dto.getCliente_id())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.getCliente_id()));

        // --- REMOVIDO: Verificação de tipo desnecessária ---
        // if (cliente.getTipo() != TipoPessoa.CLIENTE) { ... }

        Bordados bordado;

        if (id != null) {
            // Atualização
            bordado = bordadoRepositorio.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Bordado não encontrado com ID: " + id));
        } else {
            // Criação
            bordado = new Bordados();
            bordado.setDataCadastro(LocalDate.now());
        }

        // --- Atualiza/Define os campos comuns ---
        bordado.setCliente(cliente); // <-- Associa a entidade Cliente correta
        bordado.setDataEntrega(dto.getDataEntrega());
        // Boa prática: Validar se valor não é nulo ou negativo no DTO com @NotNull @Positive
        bordado.setValor(dto.getValor() != null ? dto.getValor() : BigDecimal.ZERO);
        bordado.setDescricao(dto.getDescricao());

        // Lógica de arquivo
        if (file != null && !file.isEmpty()) {
            try {
                bordado.setArquivo(file.getBytes());
                bordado.setNomeArquivo(file.getOriginalFilename());
            } catch (IOException e) {
                // Logar o erro é uma boa prática
                // logger.error("Erro ao processar upload de arquivo para bordado", e);
                throw new RuntimeException("Erro ao processar arquivo para bordado", e);
            }
        }
        // Considere adicionar lógica para *remover* o arquivo se 'file' for nulo/vazio na atualização

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

    // --- REMOVIDO: Método 'atualizarComArquivo' redundante ---
    // O 'salvarOuAtualizarComArquivo' já faz a lógica de atualização quando 'id' não é nulo.
    // Mantenha apenas um método para simplificar.

    private BordadoResponseDTO toResponseDTO(Bordados bordado) {
        BordadoResponseDTO dto = new BordadoResponseDTO();
        dto.setId(bordado.getId());
        dto.setDataCadastro(bordado.getDataCadastro());
        dto.setDataEntrega(bordado.getDataEntrega());
        dto.setValor(bordado.getValor());
        dto.setDescricao(bordado.getDescricao());
        // --- CORRIGIDO: Usar getUsername() da entidade Cliente ---
        if (bordado.getCliente() != null) {
            dto.setClienteNome(bordado.getCliente().getUsername());
        } else {
            dto.setClienteNome("Cliente não associado"); // Tratamento para caso nulo
        }
        dto.setNomeArquivo(bordado.getNomeArquivo());
        return dto;
    }
}