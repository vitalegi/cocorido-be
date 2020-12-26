package it.vitalegi.cocorido.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MyHandler {

	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public Message handleTextMessage(Message input) {
		log.info("Received message from {}: {}", input);
		String msg = ((int) (Math.random() * 100)) + " " + input.getText();
		input.setText(msg);
		return input;
	}
}
