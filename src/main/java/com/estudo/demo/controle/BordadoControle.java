package com.estudo.demo.controle;

import com.estudo.demo.DTOs.BordadoRequestDTO;
import com.estudo.demo.DTOs.BordadoResponseDTO;
import com.estudo.demo.Servico.BordadoServico;
import com.estudo.demo.model.Bordados;
import com.estudo.demo.repositorio.BordadoRepositorio;
import com.estudo.demo.repositorio.PessoaRepositorio;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bordados")
public class BordadoControle {

    private final BordadoServico bordadoServico;
    private final BordadoRepositorio bordadoRepositorio;
    private final PessoaRepositorio pessoaRepositorio;


    public BordadoControle(BordadoServico bordadoServico, BordadoRepositorio bordadoRepositorio, PessoaRepositorio pessoaRepositorio) {
        this.bordadoServico = bordadoServico;
        this.bordadoRepositorio = bordadoRepositorio;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    @PostMapping
    public ResponseEntity<BordadoResponseDTO> criar(@Valid @RequestBody BordadoRequestDTO dto) {
        BordadoResponseDTO bordados = bordadoServico.criarBordado(dto);
        return ResponseEntity.ok(bordados);
    }

    @GetMapping
    public ResponseEntity<List<BordadoResponseDTO>> listar() {
        return ResponseEntity.ok(bordadoServico.listarBordados());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BordadoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody BordadoRequestDTO dto) {
        BordadoResponseDTO atualizado = bordadoServico.atualizarBordado(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BordadoResponseDTO> buscar(@PathVariable Long id){
        return ResponseEntity.ok(bordadoServico.buscarPorId(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<BordadoResponseDTO> criarComArquivo(
            @RequestParam("cliente_id") Long clienteId,
            @RequestParam("dataEntrega") String dataEntrega,
            @RequestParam("valor") BigDecimal valor,
            @RequestParam("descricao") String descricao,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        BordadoRequestDTO dto = new BordadoRequestDTO();
        dto.setCliente_id(clienteId);
        dto.setDataEntrega(LocalDate.parse(dataEntrega));
        dto.setValor(valor);
        dto.setDescricao(descricao);
        dto.setArquivo(file.getBytes());
        dto.setNomeArquivo(file.getOriginalFilename());

        BordadoResponseDTO response = bordadoServico.criarBordado(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<BordadoResponseDTO> uploadArquivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cliente_id") Long clienteId,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "dataEntrega", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntrega,
            @RequestParam(value = "valor", required = false) BigDecimal valor) {

        BordadoRequestDTO dto = new BordadoRequestDTO();
        dto.setCliente_id(clienteId);
        dto.setDescricao(descricao);
        dto.setDataEntrega(dataEntrega);
        dto.setValor(valor);

        BordadoResponseDTO response = bordadoServico.salvarComArquivo(dto, file);
        return ResponseEntity.ok(response);
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
