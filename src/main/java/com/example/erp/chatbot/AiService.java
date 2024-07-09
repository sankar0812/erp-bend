package com.example.erp.chatbot;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class AiService {
	
	private  RestTemplate restTemplate;
    private  String apiKey;
    private  ObjectMapper objectMapper;
    private  AiRepository chatMessageRepository;

    @Autowired
    public void OpenAIService(RestTemplate restTemplate, @Value("${openai.apiKey}") String apiKey,
                         ObjectMapper objectMapper, AiRepository chatMessageRepository) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
        this.chatMessageRepository = chatMessageRepository;
    }

//    public Map<String, Object> generateText(String prompt) throws JsonProcessingException {
//        String url = "https://api.openai.com/v1/chat/completions";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);
//
//        Map<String, String> message = new HashMap<>();
//        message.put("role", "user");
//        message.put("content", prompt);
//
//        List<Map<String, String>> messages = new ArrayList<>();
//        messages.add(message);
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", "gpt-3.5-turbo");
//        requestBody.put("messages", messages);
//
//        String requestBodyJson = objectMapper.writeValueAsString(requestBody);
//
//        HttpEntity<String> request = new HttpEntity<>(requestBodyJson, headers);
//
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            try {
//                JsonNode responseJson = objectMapper.readTree(responseEntity.getBody());
//                JsonNode choicesNode = responseJson.get("choices");
//                if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
//                    JsonNode choice = choicesNode.get(0);
//                    JsonNode messageNode = choice.get("message");
//                    if (messageNode != null) {
//                        // Save the chat message to the database
//                        Ai chatMessage = new Ai();
//                        chatMessage.setRole("user");
//                        chatMessage.setContent(messageNode.get("content").asText());
//                        chatMessageRepository.save(chatMessage);
//
//                        // Prepare the response
//                        Map<String, Object> ob = new HashMap<>();
//                        ob.put("content", messageNode.get("content").asText());
//                        return ob;
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
    
    
    public Map<String, Object> generateText(@RequestBody Map<String, String> requestMap) throws JsonProcessingException {
        String userPrompt = requestMap.get("prompt");
    

        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userPrompt);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);

        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode responseJson = objectMapper.readTree(responseEntity.getBody());
                JsonNode choicesNode = responseJson.get("choices");
                if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode choice = choicesNode.get(0);
                    JsonNode messageNode = choice.get("message");
                    if (messageNode != null) {
                        // Save the user input and GPT-3 response to the database
                        Ai chatMessage = new Ai();
                        chatMessage.setRole("user");
                        chatMessage.setContent(userPrompt);
                        chatMessageRepository.save(chatMessage);

                        Ai gptResponseMessage = new Ai();
                        gptResponseMessage.setRole("Alan-Ai");
                        gptResponseMessage.setContent(messageNode.get("content").asText());
                        chatMessageRepository.save(gptResponseMessage);

                        // Prepare the response
                        Map<String, Object> response = new HashMap<>();
                        response.put("userInput", userPrompt);
                        response.put("time", chatMessage.getIntime()); 
                        response.put("gpt3Response", messageNode.get("content").asText());
                        return response;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}


