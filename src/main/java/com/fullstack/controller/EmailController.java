package com.fullstack.controller;

import com.fullstack.model.EmailModel;
import com.fullstack.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {

    @Autowired
    private IEmailService emailService;

    @PostMapping(value = "/send", consumes = "multipart/form-data")
    public ResponseEntity<String> sendEmail(
            @RequestParam("toEmail") String toEmail,
            @RequestParam(value = "ccEmail", required = false) String ccEmail,
            @RequestParam("emailSubject") String emailSubject,
            @RequestParam("emailBody") String emailBody,
            @RequestParam("senderEmail") String senderEmail,
            @RequestPart(value = "emailAttachment", required = false) MultipartFile emailAttachment) {

        EmailModel emailModel = new EmailModel();
        emailModel.setToEmail(toEmail);
        emailModel.setCcEmail(ccEmail);
        emailModel.setEmailSubject(emailSubject);
        emailModel.setEmailBody(emailBody);
        emailModel.setSenderEmail(senderEmail); // Set the sender email

        // Process the attachment if present
        if (emailAttachment != null && !emailAttachment.isEmpty()) {
            try {
                File tempFile = File.createTempFile("attachment-", emailAttachment.getOriginalFilename());
                emailAttachment.transferTo(tempFile);
                emailModel.setEmailAttachment(tempFile.getAbsolutePath());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process attachment");
            }
        }

        emailService.sendEmail(emailModel);
        return ResponseEntity.ok("Email Sent Successfully");
    }
}
