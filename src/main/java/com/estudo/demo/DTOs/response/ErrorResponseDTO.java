package com.estudo.demo.DTOs.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO (

    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path
    ){}
