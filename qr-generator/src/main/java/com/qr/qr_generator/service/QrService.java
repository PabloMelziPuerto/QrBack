package com.qr.qr_generator.service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qr.qr_generator.model.QrCode;

import repository.QrCodeRepository;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QrService {
	@Autowired
	private QrCodeRepository qrCodeRepository;

	public QrCode generateAndSaveQr(String content, String type) throws IOException, WriterException {
	    byte[] imageBytes = generateQrCode(content, 300, 300);

	    // Guardar la imagen en disco
	    UUID id = UUID.randomUUID();
	    String filename = "qr_" + id + ".png";
	    Files.createDirectories(Paths.get("qrs"));
	    Files.write(Paths.get("qrs/" + filename), imageBytes);

	    // Guardar en base de datos
	    QrCode qrCode = new QrCode();
	    qrCode.setContent(content);
	    qrCode.setType(type);
	    qrCode.setFileName(filename);
	    qrCode.setCreatedAt(LocalDateTime.now());

	    qrCodeRepository.save(qrCode);
	    return qrCode;
	}

	
	
	
	
	
	
	public byte[] generateQrCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
        
        
}
}