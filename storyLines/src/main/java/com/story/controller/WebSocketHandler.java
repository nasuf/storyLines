package com.story.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.story.model.Notification;
import com.story.repository.NotificationRepository;
import com.story.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@ServerEndpoint(value = "/websocket/{openid}")
@Component
public class WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
	private static int onlineCount = 0;

	private static CopyOnWriteArraySet<WebSocketHandler> webSocketSet = new CopyOnWriteArraySet<WebSocketHandler>();

	private Session session;
	private String openid;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static NotificationRepository nfcRepository;

	@OnOpen
	public void onOpen(Session session, @PathParam("openid") String openid) {
		this.session = session;
		this.openid = openid;
		List<WebSocketHandler> list = webSocketSet.stream()
				.filter(socket -> socket.session.getId().equals(session.getId())).collect(Collectors.toList());
		if (list.size() == 0) {
			webSocketSet.add(this);
			addOnlineCount();
//			System.out.println("Socket Client Connected! Count: [" + getOnlineCount() + "]");
			logger.debug("New Socket Client Connected! Online Users: [ {} ]", getOnlineCount());
		}

		this.notifyWithDelay(openid);
	}

	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		subOnlineCount();
//		System.out.println("Socket Closed! Count: " + getOnlineCount());
		logger.debug("Socket Client Closed! Online Users: [ {} ]", getOnlineCount());
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Socket Message Received! Content: " + message);

		for (WebSocketHandler socket : webSocketSet) {

		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
	}

	public void sendMessage(Notification nfc) throws IOException {
		if (nfc.getType().equals(Constant.MESSAGE_TYPE_MULTIPLE)) {
			this.sendBatchMessage(nfc);
		} else {
			webSocketSet.stream().filter(socket -> (socket.openid.equals(nfc.getDestination())))
			.forEach(socket -> {
				try {
					socket.session.getBasicRemote().sendText(MAPPER.writeValueAsString(nfc));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
		}
		// this.session.getAsyncRemote().sendText(message);
	}

	public void sendBatchMessage(Notification nfc) throws IOException {

		webSocketSet.stream().forEach(socket -> {
			try {
				if (this.openid != socket.openid) {
					socket.session.getBasicRemote().sendText(MAPPER.writeValueAsString(nfc));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public Boolean checkOnlineUser(String destination) {
		for (WebSocketHandler socket : webSocketSet) {
			if (socket.openid.equals(destination))
				return true;
		}
		return false;
	}

	public void notifyWithDelay(String destination) {
		List<Notification> nfcs = nfcRepository.findByDestination(destination);
		for (Notification nfc: nfcs) {
			if (!nfc.getConsumed()) {
				try {
					this.sendMessage(nfc);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketHandler.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketHandler.onlineCount--;
	}
}