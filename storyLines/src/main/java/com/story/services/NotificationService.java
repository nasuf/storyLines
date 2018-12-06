package com.story.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.story.controller.WebSocketHandler;
import com.story.model.Notification;
import com.story.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository nfcRepository;
	@Autowired
	private WebSocketHandler socketHandler;

	public void createNotificatoin(String type, String destination, Boolean isStoryUpdateNotification,
			String storyTitle) {
		Notification nfc = this.nfcRepository.findByDestinationAndIsStoryUpdateNotification(destination,
				isStoryUpdateNotification);
		if (null == nfc) {
			nfc = new Notification();
			nfc.setType(type);
			nfc.setId(UUID.randomUUID().toString());
			nfc.setDestination(destination);
			nfc.setIsStoryUpdateNotification(isStoryUpdateNotification);
			Set<String> set = new HashSet<String>();
			set.add(storyTitle);
			nfc.setStoryTitleList(set);
		} else {
			Set<String> set = nfc.getStoryTitleList();
			set.add(storyTitle);
			nfc.setStoryTitleList(set);
		}
		nfc.setConsumed(false);
		
		Boolean isOnline = socketHandler.checkOnlineUser(destination);
		if (isOnline) {
			try {
				socketHandler.sendMessage(nfc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.nfcRepository.save(nfc);
		}
	}
}
