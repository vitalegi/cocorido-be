package it.vitalegi.cocorido.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {

	private String from;
	private String text;
}