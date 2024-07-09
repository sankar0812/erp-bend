package com.example.erp.openAI;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class OpenAIController {

	private final OpenAIService openAIService;

	@Autowired
	public OpenAIController(OpenAIService openAIService) {
		this.openAIService = openAIService;
	}

	@PostMapping("/generate-text")
	public Map<String, Object> generateText(@RequestBody String prompt) throws JsonProcessingException {
		return openAIService.generateText(prompt);
	}
}
