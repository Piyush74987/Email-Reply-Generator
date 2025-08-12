package com.email.writer.repository;


import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import com.email.writer.model.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
public class EmailGenratorRepoImpl implements EmailGenratorRepo {
	
	private final WebClient wc;
	
	public EmailGenratorRepoImpl(WebClient.Builder builder) {
	    this.wc = builder.build();
	}
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	

	@Override
	public String emailReply(EmailRequest er) {
		// Build the prompt
		String prompt = buildPrompt(er);
		
		// craft request
		Map<String, Object> requestbody = Map.of(
				"contents", new Object[] {
						Map.of("parts",new Object[]{
							Map.of("text",prompt)
						})
				});
		// Do request and response
		 String  response = wc.post()
				.uri(geminiApiUrl + "?key=" + geminiApiKey)
				.header("Content-Type", "application/json")
				.bodyValue(requestbody)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		 
		 // return response
		return extractResponse(response);
	}
	
	private String extractResponse(String response) {
		try {
			ObjectMapper mapper= new ObjectMapper();
			JsonNode rootNode=mapper.readTree(response);
			return  rootNode.path("candidates")
					.get(0)
					.path("content")
					.path("parts")
					.get(0)
					.path("text")
					.asText();
		}
		catch(Exception e) {
			return "Error processing request" + e.getMessage();
		}
	}

	private String buildPrompt(EmailRequest er) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("Genrate a Professional email reply for the following email content. Dont Genrate the subject line in any case and Dont write the subject also");
		if(er.getTone()!=null && er.getTone().isEmpty()) {
			prompt.append("use a").append(er.getTone()).append("tone");
		}
		prompt.append("\n Original email:").append(er.getEmailContent());
		return prompt.toString();	
	}
	
	

}
