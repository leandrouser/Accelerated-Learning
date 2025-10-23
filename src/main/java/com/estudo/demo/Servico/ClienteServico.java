package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.ClienteRequestDTO;
import com.estudo.demo.DTOs.response.ClienteResponseDTO;
import com.estudo.demo.model.Cliente;
import com.estudo.demo.repositorio.ClienteRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServico {

    private final ClienteRepositorio clienteRepositorio;

    public ClienteServico(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @Transactional
    public ClienteResponseDTO criarCliente(ClienteRequestDTO dto) {
        validarDadosUnicos(dto, null);

        Cliente cliente = new Cliente();
        cliente.setUsername(dto.getUsername());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());
        cliente.setAtivo(true);

        Cliente salvo = clienteRepositorio.save(cliente);
        return toResponseDTO(salvo);
    }

    public List<ClienteResponseDTO> findAll() {
        return clienteRepositorio.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        validarDadosUnicos(dto, id);

        cliente.setUsername(dto.getUsername());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());

        Cliente atualizado = clienteRepositorio.save(cliente);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public void desativarCliente(Long id) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        cliente.setAtivo(false);
        clienteRepositorio.save(cliente);
    }

    private void validarDadosUnicos(ClienteRequestDTO dto, Long id) {
        // Validar CPF único
        if (id == null) {
            if (clienteRepositorio.findByCpf(dto.getCpf()).isPresent()) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
            if (clienteRepositorio.findByUsername(dto.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username já cadastrado");
            }
        } else {
            if (clienteRepositorio.existsByCpfAndIdNot(dto.getCpf(), id)) {
                throw new IllegalArgumentException("CPF já cadastrado para outro cliente");
            }
            if (clienteRepositorio.existsByUsernameAndIdNot(dto.getUsername(), id)) {
                throw new IllegalArgumentException("Username já cadastrado para outro cliente");
            }
        }
    }

    @Transactional
    public List<ClienteResponseDTO> buscarPorTermo(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return findAll();
        }

        List<Cliente> clientes = clienteRepositorio.searchClientes(termo.trim());
        return clientes.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getUsername(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.isAtivo()
        );
    }
}