package kafkafirst.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kafkafirst.service.KafkaSenderService;
import kakfafirst.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/kafka")
public class ProducerController {
	
	@Autowired
	KafkaSenderService kafkaSenderService;
	
	@PostMapping(path = "/message", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> publishMessageToTopic (@RequestBody String message) {
		log.info("[controller] message received: " + message);

		kafkaSenderService.sendMessageToTopic(message);
		
		ApiResponse rspApi = ApiResponse.builder().http_code(HttpStatus.ACCEPTED.value())
				.status(HttpStatus.ACCEPTED.toString())
				.message("Request accepted")
				.build();
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(rspApi);
		
	}
	
	
}
