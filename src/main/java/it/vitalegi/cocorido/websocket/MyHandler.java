package it.vitalegi.cocorido.websocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MyHandler {

	@MessageMapping("/chat/{boardId}")
	@SendTo("/topic/messages")
	public Message handleTextMessage(@DestinationVariable long boardId, Message input) {
		log.info("Received message from {}: {}", boardId, input);
		String msg = ((int) (Math.random() * 100)) + " " + input.getText();
		input.setText(msg);
		return input;
	}
}
