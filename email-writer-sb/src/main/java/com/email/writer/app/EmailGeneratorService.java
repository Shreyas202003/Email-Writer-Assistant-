//package com.email.writer.app;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
////import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
////import lombok.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//@Service
//public class EmailGeneratorService {
//
//    private final WebClient webClient;
//
//    public EmailGeneratorService(WebClient.Builder webClientBuilder){
//        this.webClient = webClientBuilder.build();
//    }
//
//    @Value("${gemini.api.url}")
//    private  String geminiApiUrl;
//    @Value("${gemini.api.key}")
//    private String geminiApiKey;
//    public String generateEmailReply(EmailRequest emailRequest){
//        //Build promp
//        String prompt = biuldPrompt(emailRequest);
//        //craft request
//        Map<String, Object> requestBody = Map.of(
//                 "contents", new Object[]{
//                         Map.of("parts", new Object[]{
//                                 Map.of("text", prompt)
//                         })
//                }
//        );
//        //Do request and get response
//        String response = webClient.post()
//                .uri(geminiApiUrl+geminiApiKey)
//                .header("Content-Type","application/json")
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        //Extract response and return
//        return extractResponseContent(response);
//    }
//
//    private String extractResponseContent(String response) {
//        try{
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootnode = mapper.readTree((response));
//            return rootnode.path("candidates")
//                    .get(0)
//                    .path("content")
//                    .path("parts")
//                    .get(0)
//                    .path("text")
//                    .asText();
//        }catch(Exception e){
//            return "Error processing request: " +e.getMessage();
//        }
//    }
//
//    private String biuldPrompt(EmailRequest emailRequest) {
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("Generate a professional email reply for the following email content.. ");
//        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()){
//            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
//        }
//        prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent()) ;
//        return prompt.toString();
//    }
//
//}



package com.email.writer.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;

    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String generateEmailReply(EmailRequest emailRequest) {
        // Build prompt
        String prompt = buildPrompt(emailRequest);

        // Craft request body
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // Build the URI with the API key as a query parameter
        String uri = UriComponentsBuilder
                .fromUriString(geminiApiUrl.trim()) // Ensure no leading/trailing spaces
                .queryParam("key", geminiApiKey.trim()) // Add API key as query param
                .build()
                .toUriString();

        // Do request and get response
        String response = webClient.post()
                .uri(uri)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Extract response and return
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content. ");
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
        }
        prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
