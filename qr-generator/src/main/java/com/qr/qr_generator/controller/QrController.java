package com.qr.qr_generator.controller;
import com.google.zxing.WriterException;
import com.qr.qr_generator.dto.QrResponse;
import com.qr.qr_generator.model.QrCode;
import com.qr.qr_generator.service.QrService;

import repository.QrCodeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import java.io.IOException;

@RestController
@RequestMapping("/api/qr")
public class QrController {
	
	@Autowired
    private QrService qrService;
	@Autowired
	private QrCodeRepository qrCodeRepository;

	@CrossOrigin(origins = "https://misterqrgenerator.com")
	@PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QrResponse> generateQr(@RequestBody String text) throws WriterException, IOException {
	    QrCode qrCode = qrService.generateAndSaveQr(text, "TEXT");

	    String baseUrl = "https://qrback-2.onrender.com/api/qr/view/";
	    QrResponse response = new QrResponse(
	            qrCode.getId().toString(),
	            baseUrl + qrCode.getId()
	    );

	    return ResponseEntity.ok(response);
	}
	@CrossOrigin(origins = "https://misterqrgenerator.com")
	@PostMapping(value = "/generate/url", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QrResponse> generateQrFromUrl(@RequestBody String url) throws IOException, WriterException {
	    QrCode qrCode = qrService.generateAndSaveQr(url, "URL");

	    String baseUrl = "https://qrback-2.onrender.com/api/qr/view/";
	    QrResponse response = new QrResponse(
	            qrCode.getId().toString(),
	            baseUrl + qrCode.getId()
	    );

	    return ResponseEntity.ok(response);
	}

	@CrossOrigin(origins = "https://misterqrgenerator.com")
	@PostMapping(value = "/generate/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QrResponse> generateQrFromPdf(@RequestParam("file") MultipartFile file) throws IOException, WriterException {
	    if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
	        return ResponseEntity.badRequest().build();
	    }

	    // Guardar el archivo PDF en disco
	    String pdfName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	    Files.createDirectories(Paths.get("pdfs"));
	    Path pdfPath = Paths.get("pdfs/" + pdfName);
	    Files.write(pdfPath, file.getBytes());

	    // Generar QR apuntando a la URL del PDF (simulada aquí como local)
	    String pdfUrl = "https://qrback-2.onrender.com/api/pdf/" + pdfName;
	    QrCode qrCode = qrService.generateAndSaveQr(pdfUrl, "PDF");

	    String imageUrl = "https://qrback-2.onrender.com/api/qr/view/" + qrCode.getId();
	    QrResponse response = new QrResponse(qrCode.getId().toString(), imageUrl);
	    return ResponseEntity.ok(response);
	}
	@CrossOrigin(origins = "https://misterqrgenerator.com")
	@GetMapping(value = "/pdf/{filename}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> getPdf(@PathVariable String filename) throws IOException {
	    Path filePath = Paths.get("pdfs/" + filename);
	    if (!Files.exists(filePath)) return ResponseEntity.notFound().build();

	    byte[] content = Files.readAllBytes(filePath);
	    return ResponseEntity.ok(content);
	}
	@CrossOrigin(origins = "https://misterqrgenerator.com")
	@GetMapping(value = "/download/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> downloadQrById(@PathVariable UUID id) throws IOException {
	    QrCode qrCode = qrCodeRepository.findById(id).orElse(null);
	    if (qrCode == null) return ResponseEntity.notFound().build();

	    Path qrPath = Paths.get("qrs/" + qrCode.getFileName());
	    byte[] qrImage = Files.readAllBytes(qrPath);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    headers.setContentLength(qrImage.length);
	    headers.setContentDispositionFormData("attachment", qrCode.getFileName()); // 👈 esto fuerza la descarga

	    return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}


	@CrossOrigin(origins = "https://misterqrgenerator.com")
	@GetMapping(value = "/view/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getQrById(@PathVariable UUID id) throws IOException {
	    QrCode qrCode = qrCodeRepository.findById(id).orElse(null);
	    if (qrCode == null) return ResponseEntity.notFound().build();

	    Path qrPath = Paths.get("qrs/" + qrCode.getFileName());
	    byte[] qrImage = Files.readAllBytes(qrPath);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    headers.setContentLength(qrImage.length);
	    headers.setCacheControl(CacheControl.noCache().getHeaderValue()); // opcional

	    return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}


}
