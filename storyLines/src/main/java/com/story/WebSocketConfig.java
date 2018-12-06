package com.story;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.story.controller.WebSocketHandler;
import com.story.repository.NotificationRepository;

@Configuration
public class WebSocketConfig {
	
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
	
	@Autowired
	public void setMessageRepository(NotificationRepository nfcRepository) {
		WebSocketHandler.nfcRepository = nfcRepository;
	}
}
