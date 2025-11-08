package com.estudo.demo.execoes;

import com.estudo.demo.DTOs.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 🔒 Violação de integridade (chave duplicada, campos nulos, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Violação de integridade nos dados.";
        String detalhes = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();

        if (detalhes != null) {

            // Duplicidade de registro
            if (detalhes.contains("duplicar valor da chave") || detalhes.contains("duplicate key")) {
                message = "Já existe um registro com esses dados.";

                if (detalhes.contains("product_name")) {
                    int start = detalhes.indexOf('(');
                    int end = detalhes.indexOf(')', start);
                    if (start != -1 && end != -1) {
                        String produtoDuplicado = detalhes.substring(start + 1, end);
                        message = "O produto " + produtoDuplicado + " já está cadastrado.";
                    }
                }
            }

            // Campo obrigatório nulo
            else if (detalhes.contains("null value") || detalhes.contains("cannot be null")) {
                String campo = "um campo obrigatório";
                int start = detalhes.indexOf("\"");
                int end = detalhes.indexOf("\"", start + 1);
                if (start != -1 && end != -1) {
                    campo = detalhes.substring(start + 1, end);
                }
                message = "O campo " + campo + " não pode ser nulo.";
            }
        }

        ErrorResponse error = new ErrorResponse("BAD_REQUEST", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ⚙️ Validação de campos (Bean Validation: @NotNull, @Size, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de validação");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        body.put("fields", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    // 🧩 Fallback genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
