package com.hiringwire.api;

import com.hiringwire.model.Cv;
import com.hiringwire.service.CvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cv")
@CrossOrigin
@RequiredArgsConstructor
public class CvController {
    private final CvService cvService;

    @PostMapping
    public Cv saveCv(@RequestBody Cv cv) {
        return cvService.saveCv(cv);
    }

    @GetMapping("/user/{userId}")
    public List<Cv> getCvsByUserId(@PathVariable Long userId) {
        return cvService.getCvsByUserId(userId);
    }

    @GetMapping("/{id}")
    public Cv getCvById(@PathVariable Long id) {
        return cvService.getCvById(id);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        Cv cv = cvService.getCvById(id);
        byte[] pdf = cvService.generatePdf(cv);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cv.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}