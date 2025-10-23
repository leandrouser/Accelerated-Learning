package com.estudo.demo.controle;

import com.estudo.demo.DTOs.requestDTO.VendaRequestDTO;
import com.estudo.demo.DTOs.response.VendaResponseDTO;
import com.estudo.demo.Servico.VendaServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaControle {

    private final VendaServico vendaServico;

    public VendaControle(VendaServico vendaServico) {
        this.vendaServico = vendaServico;
    }

    @PostMapping
    public ResponseEntity<VendaResponseDTO> criarVenda(@RequestBody VendaRequestDTO dto){
        return ResponseEntity.ok(vendaServico.criarVenda(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarVenda(@PathVariable Long id){
        VendaResponseDTO vendaDTO = vendaServico.buscarPorId(id); // chama o serviço
        return ResponseEntity.ok(vendaDTO);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaResponseDTO>> buscarVendasPorCliente(@PathVariable Long clienteId) {
        List<VendaResponseDTO> vendas = vendaServico.buscarVendasPorCliente(clienteId);
        return ResponseEntity.ok(vendas);
    }

}
