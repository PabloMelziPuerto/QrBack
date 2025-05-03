package com.qr.qr_generator.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class QrCode {
	@Id
    @GeneratedValue
    private UUID id;

    private String content;       // Texto, URL o path al PDF
    private String type;          // TEXT | URL | PDF
    private String fileName;      // Nombre del archivo QR generado
    private LocalDateTime createdAt;
}
