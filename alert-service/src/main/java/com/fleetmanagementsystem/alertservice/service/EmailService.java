package com.fleetmanagementsystem.alertservice.service;

import com.fleetManagementSystem.commons.alert.AlertVehicle;
import com.fleetmanagementsystem.alertservice.email.EmailTemplates;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private final KafkaTemplate<String, AlertVehicle> kafkaTemplate; // KafkaTemplate for producing messages

    @Async("taskExecutor")
    public void sendOrderConfirmationEmail(
            String to, String subject, Map<String,Object> alertList
    ) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("youssefmennani1996@gmail.com");

        final String templateName = EmailTemplates.ALERT_NOTIF_VL.getTemplate();

        Context context = new Context();
        context.setVariables(alertList);
        messageHelper.setSubject(EmailTemplates.ALERT_NOTIF_VL.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(to);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", to, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", to);
        }

    }

    @Async("taskExecutor")
    public void notifyVehicleAlert(AlertVehicle alertVehicle){
        kafkaTemplate.send("notify-topic", alertVehicle);
        log.info("Notification sent to Kafka: {}", alertVehicle);
    }

}
