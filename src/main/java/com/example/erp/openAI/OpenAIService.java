package com.example.erp.openAI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

	private final RestTemplate restTemplate;
	private final String apiKey;
	private final ObjectMapper objectMapper;

	public OpenAIService(RestTemplate restTemplate, @Value("${openai.apiKey}") String apiKey,
			ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.apiKey = apiKey;
		this.objectMapper = objectMapper;
	}
	public Map<String, Object> generateText(String prompt) throws JsonProcessingException {
	    String url = "https://api.openai.com/v1/chat/completions";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + apiKey);

	    Map<String, String> message = new HashMap<>();
	    message.put("role", "user");
	    message.put("content", prompt);

	    List<Map<String, String>> messages = new ArrayList<>();
	    messages.add(message);

	    Map<String, Object> requestBody = new HashMap<>();
	    requestBody.put("model", "gpt-3.5-turbo");
	    requestBody.put("messages", messages);

	    String requestBodyJson = objectMapper.writeValueAsString(requestBody);

	    HttpEntity<String> request = new HttpEntity<>(requestBodyJson, headers);

	    ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
	    if (responseEntity.getStatusCode() == HttpStatus.OK) {
	        try {
	            JsonNode responseJson = objectMapper.readTree(responseEntity.getBody());
	            JsonNode choicesNode = responseJson.get("choices");
	            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
	                JsonNode choice = choicesNode.get(0);
	                JsonNode messageNode = choice.get("message");
	                if (messageNode != null) {
	                    Map<String, Object> ob = new HashMap<>();
	                    ob.put("content", messageNode.get("content").asText());
	                    return ob;
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}

}