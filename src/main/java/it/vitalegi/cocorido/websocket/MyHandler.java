package it.vitalegi.cocorido.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MyHandler {

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/chat")
	// @SendTo("/topic/messages")
	public void handleTextMessage(Message input) {
		log.info("Received message from {}: {}", input);
		String msg = ((int) (Math.random() * 100)) + " " + input.getText();
		input.setText(msg);
		template.convertAndSend("/topic/messages", input);
		//return input;
	}
}
