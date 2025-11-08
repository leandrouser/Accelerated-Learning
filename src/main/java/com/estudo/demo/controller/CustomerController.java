package com.estudo.demo.controller;

import com.estudo.demo.DTOs.requestDTO.CustomerRequestDTO;
import com.estudo.demo.DTOs.response.CustomerResponseDTO;
import com.estudo.demo.Servico.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> criar(@Valid @RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO cliente = customerService.criarCliente(dto);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> listar() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO dto) {
        return ResponseEntity.ok(customerService.atualizar(id, dto));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDTO>> buscarPorTermo(@RequestParam String termo) {
        List<CustomerResponseDTO> resultados = customerService.buscarPorTermo(termo);
        return ResponseEntity.ok(resultados);
    }

}