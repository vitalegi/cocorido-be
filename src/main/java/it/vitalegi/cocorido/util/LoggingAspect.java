package it.vitalegi.cocorido.util;

import java.util.concurrent.atomic.AtomicLong;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

	private static AtomicLong ID = new AtomicLong();

	private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	protected String getId() {
		return ID.incrementAndGet() + "";
	}

	@Around("@annotation(LogExecutionTime)")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

		String id = getId();

		long startTime = System.currentTimeMillis();
		try {
			Object out = joinPoint.proceed();
			logger.info(messageEndOk(id, joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), System.currentTimeMillis() - startTime));

			if (logger.isDebugEnabled()) {
				for (int i = 0; i < joinPoint.getArgs().length; i++) {
					logger.debug("{} Argument [{}]: {}", id, i, joinPoint.getArgs()[i]);
				}
				logger.debug("{} Result: {}", id, out);
			}
			return out;
		} catch (Throwable e) {
			logger.error(messageEndError(id, joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), System.currentTimeMillis() - startTime, e.getClass().getName(),
					e.getMessage()));
			if (logger.isInfoEnabled()) {
				for (int i = 0; i < joinPoint.getArgs().length; i++) {
					logger.info("{} Argument [{}]: {}", id, i, joinPoint.getArgs()[i]);
				}
			}
			throw e;
		}
	}

	public void logMethodEnd() {

		String id = ID.incrementAndGet() + "";

		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		logger.info("{} End Invoke: {}", id, stackTrace[1].getMethodName());
	}

	public void logMethodStart() {

		String id = ID.incrementAndGet() + "";

		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		logger.info("{} Start Invoke: {}", id, stackTrace[1].getMethodName());
	}

	protected String messageEndError(String id, String className, String methodNme, long duration, String error,
			String errorMessage) {
		return String.format("%s - End Invoke:   %s | %s | Result: %s | Duration: %dms | Error: %s | Message: %s", //
				id, className, methodNme, "KO", duration, error, errorMessage);
	}

	protected String messageEndOk(String id, String className, String methodNme, long duration) {
		return String.format("%s - End Invoke:   %s | %s | Result: %s | Duration: %dms", //
				id, className, methodNme, "OK", duration);
	}

}