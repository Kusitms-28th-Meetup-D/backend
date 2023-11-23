package com.kusithm.meetupd.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;


    @Async
    public void sendReceivedReviewEmail(String toEmail, String teamName) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createReceivedReviewEmailForm(toEmail, teamName);
        emailSender.send(emailForm);
    }

    @Async
    public void sendJoinTeamEmail(String toEmail, String teamName, String kakaoUrl) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createJoinTeamEmailForm(toEmail, teamName, kakaoUrl);
        emailSender.send(emailForm);
    }

    @Async
    public void sendEndTeamEmail(String toEmail, String teamName) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createEndTeamEmailForm(toEmail, teamName);
        emailSender.send(emailForm);
    }

    private MimeMessage createReceivedReviewEmailForm(String toEmail, String teamName) throws MessagingException, UnsupportedEncodingException {
        String setFrom = "ojy09293@gmail.com";
        String title = "[Wanteam] 신규 추천사 등록 안내";
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(title);
        message.setFrom(new InternetAddress(setFrom, "Wanteam", "UTF-8"));
        message.setText(setContextReviewedMail(teamName), "utf-8", "html");
        return message;
    }

    private String setContextReviewedMail(String teamName) {
        Context context = new Context();
        context.setVariable("teamName", teamName);
        return templateEngine.process("new-review-mail", context);
    }


    private MimeMessage createJoinTeamEmailForm(String toEmail, String teamName, String kakaoUrl) throws MessagingException, UnsupportedEncodingException {
        String setFrom = "ojy09293@gmail.com";
        String title = "[Wanteam] 팀 합류 안내 메일";
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(title);
        message.setFrom(new InternetAddress(setFrom, "Wanteam", "UTF-8"));
        message.setText(setContextJoinTeamMail(teamName, kakaoUrl), "utf-8", "html");
        return message;
    }

    private String setContextJoinTeamMail(String teamName, String kakaoUrl) {
        Context context = new Context();
        context.setVariable("teamName", teamName);
        context.setVariable("kakaoUrl", kakaoUrl);
        return templateEngine.process("join-team-mail", context);
    }


    private MimeMessage createEndTeamEmailForm(String toEmail, String teamName) throws MessagingException, UnsupportedEncodingException {
        String setFrom = "ojy09293@gmail.com";
        String title = "[Wanteam] 팀 활동 종료 안내 메일";
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(title);
        message.setFrom(new InternetAddress(setFrom, "Wanteam", "UTF-8"));
        message.setText(setContextEndTeamMail(teamName), "utf-8", "html");
        return message;
    }

    private String setContextEndTeamMail(String teamName) {
        Context context = new Context();
        context.setVariable("teamName", teamName);
        return templateEngine.process("team-review-date-end-mail", context);
    }
}
