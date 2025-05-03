package com.qr.qr_generator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QrResponse {
	private String id;
    private String imageUrl;
}
