package it.vitalegi.cocorido;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerContextWebRequestInterceptor implements WebRequestInterceptor {

	@Override
	public void preHandle(WebRequest request) throws Exception {
		MDC.put("userId", request.getHeader("userId"));
		MDC.put("requestId", UUID.randomUUID().toString());
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
	}

}
