package org.example.easyrecruitbackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.easyrecruitbackend.dto.EmailDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Transactional
    public void sendStyledEmail(EmailDTO emailDTO) throws MessagingException {
        // Créer le contexte Thymeleaf
        Context context = new Context();
        context.setVariables(emailDTO.getVariables());

        // Générer le contenu HTML de l'email
        String htmlContent = templateEngine.process("email-template", context);

        // Configurer et envoyer l'email
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(emailDTO.getTo());
        helper.setSubject(emailDTO.getSubject());
        helper.setText(htmlContent, true);
        helper.setFrom("easyrecruit93@gmail.com");

        mailSender.send(mimeMessage);

    }

}
