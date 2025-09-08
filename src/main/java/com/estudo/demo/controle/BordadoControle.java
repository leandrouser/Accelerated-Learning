package com.estudo.demo.controle;

import com.estudo.demo.DTOs.requestDTO.BordadoRequestDTO;
import com.estudo.demo.DTOs.response.BordadoResponseDTO;
import com.estudo.demo.Servico.BordadoServico;
import com.estudo.demo.model.Bordados;
import com.estudo.demo.repositorio.BordadoRepositorio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bordados")
public class BordadoControle {

    private final BordadoServico bordadoServico;
    private final BordadoRepositorio bordadoRepositorio;

    public BordadoControle(BordadoServico bordadoServico, BordadoRepositorio bordadoRepositorio) {
        this.bordadoServico = bordadoServico;
        this.bordadoRepositorio = bordadoRepositorio;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<BordadoResponseDTO> criar(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("cliente_id") Long clienteId,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "dataEntrega", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntrega,
            @RequestParam(value = "valor", required = false) BigDecimal valor) {

        BordadoRequestDTO dto = new BordadoRequestDTO();
        dto.setCliente_id(clienteId);
        dto.setDescricao(descricao);
        dto.setDataEntrega(dataEntrega);
        dto.setValor(valor);

        return ResponseEntity.ok(bordadoServico.salvarOuAtualizarComArquivo(null, dto, file));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BordadoResponseDTO> atualizarComArquivo(
            @PathVariable Long id,
            @RequestParam("cliente_id") Long clienteId,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "dataEntrega", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntrega,
            @RequestParam(value = "valor", required = false) BigDecimal valor,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        BordadoRequestDTO dto = new BordadoRequestDTO();
        dto.setCliente_id(clienteId);
        dto.setDescricao(descricao);
        dto.setDataEntrega(dataEntrega);
        dto.setValor(valor);

        BordadoResponseDTO atualizado = bordadoServico.salvarOuAtualizarComArquivo(id, dto, file);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping
    public ResponseEntity<List<BordadoResponseDTO>> listar() {
        return ResponseEntity.ok(bordadoServico.listarBordados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BordadoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bordadoServico.buscarPorId(id));
    }

    @GetMapping("/{id}/arquivo")
    public ResponseEntity<byte[]> downloadArquivo(@PathVariable Long id) {
        Bordados bordado = bordadoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bordado não encontrado"));

        if (bordado.getArquivo() == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + bordado.getNomeArquivo() + "\"")
                .body(bordado.getArquivo());
    }
}
