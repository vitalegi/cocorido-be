package it.vitalegi.cocorido.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardPublisher {

	@Autowired
	private SimpMessagingTemplate template;

	public void notifyUpdate(long boardId, String message) {
		log.info("Send a notification on board {}: {}", boardId, message);
		template.convertAndSend(boardTopic(boardId), message);
	}

	protected String boardTopic(long boardId) {
		return "/topic/boards/" + boardId;
	}
}
