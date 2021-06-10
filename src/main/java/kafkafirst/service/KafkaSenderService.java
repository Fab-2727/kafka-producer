package kafkafirst.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaSenderService {
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendMessageToTopic(String message) {
		// kafkaTemplate.sendDefault(message);
		// Handling with callback for async proccesing
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.sendDefault(message);
		
	    future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

	        @Override
	        public void onSuccess(SendResult<String, String> result) {
	            log.debug("Sent message=[" + message + 
	              "] with offset=[" + result.getRecordMetadata().offset() + "]");
	        }
	        @Override
	        public void onFailure(Throwable ex) {
	            log.debug("Unable to send message=[" 
	              + message + "] due to : " + ex.getMessage());
	        }
	    });
	    
	    
	}

}
