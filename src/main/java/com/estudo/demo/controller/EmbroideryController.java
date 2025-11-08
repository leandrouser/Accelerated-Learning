package com.estudo.demo.controller;

import com.estudo.demo.Servico.EmbroideryService;
import com.estudo.demo.model.Enbroidery;
import com.estudo.demo.repository.EmbroideryRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bordados")
public class EmbroideryController {

    private final EmbroideryService embroideryService;
    private final EmbroideryRepository embroideryRepository;

    public EmbroideryController(EmbroideryService embroideryService, EmbroideryRepository embroideryRepository) {
        this.embroideryService = embroideryService;
        this.embroideryRepository = embroideryRepository;
    }

    @GetMapping("/{id}/arquivo")
    public ResponseEntity<byte[]> downloadArquivo(@PathVariable Long id) {
        Enbroidery bordado = embroideryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bordado não encontrado"));

        if (bordado.getArquivo() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + bordado.getNomeArquivo() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(bordado.getArquivo());
    }

}
