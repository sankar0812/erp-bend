package com.example.erp.controller.EmailServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

	@Autowired
	private JavaMailSender javaMailSender;
	
	 @Value("${spring.mail.username}")
	    private String from;

//	public void sendEmail(String to, String subject, String text) {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(text);
//
//		javaMailSender.send(message);
//	}
	
	  public void sendEmail(String to, String subject, String text) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom(from); // Set the from address
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(text);

	        javaMailSender.send(message);
	    }
}
