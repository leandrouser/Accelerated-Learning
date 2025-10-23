package com.estudo.demo.controle;

import com.estudo.demo.DTOs.requestDTO.ClienteRequestDTO;
import com.estudo.demo.DTOs.response.ClienteResponseDTO;
import com.estudo.demo.Servico.ClienteServico;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteControle {

    private final ClienteServico clienteServico;

    public ClienteControle(ClienteServico clienteServico) {
        this.clienteServico = clienteServico;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO cliente = clienteServico.criarCliente(dto);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(clienteServico.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteServico.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(clienteServico.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        clienteServico.desativarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorTermo(@RequestParam String termo) {
        List<ClienteResponseDTO> resultados = clienteServico.buscarPorTermo(termo);
        return ResponseEntity.ok(resultados);
    }

}