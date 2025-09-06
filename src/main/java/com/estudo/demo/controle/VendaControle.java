package com.estudo.demo.controle;

import com.estudo.demo.DTOs.VendaRequestDTO;
import com.estudo.demo.DTOs.VendaResponseDTO;
import com.estudo.demo.Servico.VendaServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendas")
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

}
