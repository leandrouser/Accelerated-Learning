package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.EmbroideryRequestDTO;
import com.estudo.demo.DTOs.response.EmbroideryResponseDTO;
import com.estudo.demo.model.Enbroidery;
import com.estudo.demo.model.Customer;
import com.estudo.demo.repository.EmbroideryRepository;
import com.estudo.demo.repository.CustomerRepository;
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
public class EmbroideryService {

    private final EmbroideryRepository embroideryRepository;
    private final CustomerRepository customerRepository;

    public EmbroideryService(EmbroideryRepository embroideryRepository, CustomerRepository customerRepository) {
        this.embroideryRepository = embroideryRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public EmbroideryResponseDTO salvarOuAtualizarComArquivo(Long id, EmbroideryRequestDTO dto, MultipartFile file) {
        // --- Busca cliente ---
        Customer customer = customerRepository.findById(dto.customer_id())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.customer_id()));

        Enbroidery bordado;

        if (id != null) {
            // Atualização
            bordado = embroideryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Bordado não encontrado com ID: " + id));
        } else {
            // Novo registro
            bordado = new Enbroidery();
            bordado.setDataCadastro(LocalDate.now());
        }

        // --- Atribuição dos campos básicos ---
        bordado.setCustomer(customer);
        bordado.setDataEntrega(dto.dateDelivery());
        bordado.setValue(dto.value() != null ? dto.value() : BigDecimal.ZERO);
        bordado.setDescricao(dto.discription());

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
        Enbroidery salvo = embroideryRepository.save(bordado);
        return toResponseDTO(salvo);
    }

    public List<EmbroideryResponseDTO> listarBordados() {
        return embroideryRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EmbroideryResponseDTO buscarPorId(Long id) {
        Enbroidery bordado = embroideryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bordado não encontrado com ID: " + id));
        return toResponseDTO(bordado);
    }

    public byte[] obterArquivo(Long id) {
        Enbroidery bordado = embroideryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bordado não encontrado com ID: " + id));

        if (bordado.getArquivo() == null) {
            throw new EntityNotFoundException("Este bordado não possui arquivo associado.");
        }

        return bordado.getArquivo();
    }

    private EmbroideryResponseDTO toResponseDTO(Enbroidery bordado) {
        return new EmbroideryResponseDTO(
                bordado.getId(),
                bordado.getDataCadastro(),
                bordado.getDataEntrega(),
                bordado.getValue(),
                bordado.getDescricao(),
                bordado.getNomeArquivo(),
                bordado.getCustomer() != null ? bordado.getCustomer().getUsername() : "Cliente não associado"
        );
    }
}
