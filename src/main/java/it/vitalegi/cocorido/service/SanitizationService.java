package it.vitalegi.cocorido.service;

import org.springframework.stereotype.Service;

@Service
public class SanitizationService {

	private static final char[] FAULT_CHARS = { '\n', '\r', '\\', '<', '>' };

	public String sanitize(String text) {
		text = text.trim();
		for (int i = 0; i < FAULT_CHARS.length; i++) {
			text = text.replace(FAULT_CHARS[i], '?');
		}
		return text;
	}
}
