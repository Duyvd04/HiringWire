package com.hiringwire.service;

import com.hiringwire.model.Cv;
import com.hiringwire.model.User;
import com.hiringwire.repository.CvRepository;
import com.hiringwire.repository.UserRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CvService {
    private final CvRepository cvRepository;
    private final UserRepository userRepository;

    public Cv saveCv(Cv cv) {
        return cvRepository.save(cv);
    }

    public List<Cv> getCvsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cvRepository.findByUser(user);
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