package it.vitalegi.cocorido.util;

import org.slf4j.MDC;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerUtil {

	public static String setContext(String groupId) {
		String oldContext = getContext();
		MDC.put("groupId", groupId);
		return oldContext;
	}

	public static String getContext() {
		return MDC.get("groupId");
	}
}
