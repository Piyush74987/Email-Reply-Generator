package com.email.writer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.email.writer.model.EmailRequest;
import com.email.writer.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailWriterController {
	
	
	private final EmailService es;
    EmailWriterController(EmailService es) {
        this.es = es;
    }
	
	

	@PostMapping("/generate")
	public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailrequest){
		String Response = es.emailReply(emailrequest);
		return ResponseEntity.ok(Response);
	}

}