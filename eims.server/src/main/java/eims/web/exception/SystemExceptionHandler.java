package eims.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import eims.web.constants.BxConstants;
import eims.web.constants.BxMessages;
import eims.web.dto.ui.ErrorResponse;
import eims.web.utils.ExceptionUtils;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class SystemExceptionHandler {

	final Logger logger = LoggerFactory.getLogger(getClass());


	@SuppressWarnings("rawtypes")
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity handleException(NullPointerException e) {
		ErrorResponse errResponse = new ErrorResponse();
		errResponse.setResponseStatus(BxConstants.Status.ERROR);
		errResponse.setMessage(BxMessages.Error.SYSTEM_EXCEPTION.getMessage());
		errResponse.setParameters(new String[] { e.getMessage() });
		errResponse.setStackTrace(ExceptionUtils.instance().printException(e));

		logger.debug(ExceptionUtils.instance().printException(e));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
	}


	@SuppressWarnings("rawtypes")
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handleException(RuntimeException e) {
		ErrorResponse errResponse = new ErrorResponse();
		errResponse.setResponseStatus(BxConstants.Status.ERROR);
		errResponse.setMessage(BxMessages.Error.SYSTEM_EXCEPTION.getMessage());
		errResponse.setParameters(new String[] { e.getMessage() });
		errResponse.setStackTrace(ExceptionUtils.instance().printException(e));

		logger.debug(ExceptionUtils.instance().printException(e));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
	}


	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(Exception e) {
		ErrorResponse errResponse = new ErrorResponse();
		errResponse.setResponseStatus(BxConstants.Status.ERROR);
		errResponse.setMessage(BxMessages.Error.SYSTEM_EXCEPTION.getMessage());
		errResponse.setParameters(new String[] { e.getMessage() });
		errResponse.setStackTrace(ExceptionUtils.instance().printException(e));

		logger.debug(ExceptionUtils.instance().printException(e));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
	}
}
