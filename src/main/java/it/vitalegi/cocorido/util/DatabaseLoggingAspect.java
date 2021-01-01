package it.vitalegi.cocorido.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class DatabaseLoggingAspect {

	@Around("within(org.springframework.data.repository.CrudRepository+)")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		try {
			Object out = joinPoint.proceed();
			log.info(messageEndOk(getCallerClassName(joinPoint), joinPoint.getSignature().getName(),
					System.currentTimeMillis() - startTime));

			if (log.isDebugEnabled()) {
				for (int i = 0; i < joinPoint.getArgs().length; i++) {
					log.debug("Argument [{}]: {}", i, joinPoint.getArgs()[i]);
				}
			}
			return out;
		} catch (Throwable e) {
			log.error(
					messageEndError(joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
							System.currentTimeMillis() - startTime, e.getClass().getName(), e.getMessage()));
			throw e;
		}
	}

	protected String getCallerClassName(ProceedingJoinPoint joinPoint) {
		Class<?>[] interfaces = joinPoint.getThis().getClass().getInterfaces();
		for (Class<?> i : interfaces) {
			if (i != null && i.getName().contains("it.vitalegi")) {
				return i.getName();
			}
		}
		return null;
	}

	protected String messageEndError(String className, String methodNme, long duration, String error,
			String errorMessage) {
		return String.format("End Invoke:   %s | %s | Result: %s | Duration: %dms | Error: %s | Message: %s", //
				className, methodNme, "KO", duration, error, errorMessage);
	}

	protected String messageEndOk(String className, String methodNme, long duration) {
		return String.format("End Invoke:   %s | %s | Result: %s | Duration: %dms", //
				className, methodNme, "OK", duration);
	}

}
