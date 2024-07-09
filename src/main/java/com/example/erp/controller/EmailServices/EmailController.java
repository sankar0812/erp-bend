package com.example.erp.controller.EmailServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class EmailController {

	@Autowired
	private EmailSender emailService;

//	@PostMapping("/send")
//	public String sendEmail(@RequestBody EmailRequest emailRequest) {
//		String to = emailRequest.getEmail();
//		String subject = emailRequest.getUserName() + " - " + emailRequest.getDescription();
//		String text = "Mobile Number: " + emailRequest.getMobileNumber() + "\n" + "Description: "
//				+ emailRequest.getDescription() + "\n" + "Email: " + emailRequest.getEmail() + "\n" + "Needs: "
//				+ emailRequest.getNeeds() + "\n" + emailRequest.formatRequirements();
//
//		emailService.sendEmail(to, subject, text);
//		return "Email sent successfully!";
//	}


	    @PostMapping("/send")
	    public String sendEmail(@RequestBody EmailRequest emailRequest) {
	        String to = emailRequest.getEmail();
	        String subject = emailRequest.getUserName() + " - " + emailRequest.getDescription();
	        String text = "Mobile Number: " + emailRequest.getMobileNumber() + "\n" + "Description: "
	                + emailRequest.getDescription() + "\n" + "Email: " + emailRequest.getEmail() + "\n" + "Needs: "
	                + emailRequest.getNeeds() + "\n" + emailRequest.formatRequirements();

	        emailService.   sendEmail(to, subject, text);
	        return "Email sent successfully!";
	    }
}
