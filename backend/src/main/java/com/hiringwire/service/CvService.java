package com.hiringwire.service;

import com.hiringwire.entity.Cv;
import com.hiringwire.repository.ICvRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class CvService {
    @Autowired
    private ICvRepository cvRepository;

    public Cv saveCv(Cv cv) {
        return cvRepository.save(cv);
    }

    public List<Cv> getCvsByUserId(Long userId) {
        return cvRepository.findByUserId(userId);
    }

    public Cv getCvById(Long id) {
        return cvRepository.findById(id).orElseThrow(() -> new RuntimeException("CV not found"));
    }

    public byte[] generatePdf(Cv cv) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("CV"));
            document.add(new Paragraph(cv.getPersonalInfo()));
            document.add(new Paragraph(cv.getEducation()));
            document.add(new Paragraph(cv.getExperience()));
            document.add(new Paragraph(cv.getSkills()));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}