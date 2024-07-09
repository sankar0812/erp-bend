package com.example.erp.chatbot;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.fasterxml.jackson.core.JsonProcessingException;
@RestController
@CrossOrigin
public class AiController {
	

	private final AiService openAIService;

	@Autowired
	public AiController(AiService openAIService) {
		this.openAIService = openAIService;
	}

	  @PostMapping("/generate-text1")
	    public Map<String, Object> generateText(@RequestBody Map<String, String> prompt) throws JsonProcessingException {
	        return openAIService.generateText(prompt);
	    }

}
