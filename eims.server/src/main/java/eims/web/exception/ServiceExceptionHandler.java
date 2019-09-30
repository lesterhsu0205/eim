package eims.web.exception;

import java.sql.SQLException;

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
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceExceptionHandler {

	final Logger logger = LoggerFactory.getLogger(getClass());


	@SuppressWarnings("rawtypes")
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity handleException(ServiceException e) {
		ErrorResponse errResponse = new ErrorResponse();
		errResponse.setResponseStatus(e.getStatus());
		errResponse.setMessage(e.getMessage());
		errResponse.setParameters(e.getParameter());
		errResponse.setStackTrace(ExceptionUtils.instance().printException(e));

		logger.debug(ExceptionUtils.instance().printException(e));

		return ResponseEntity.status(e.getHttpStatus()).body(errResponse);
	}


	@SuppressWarnings("rawtypes")
	@ExceptionHandler(SQLException.class)
	public ResponseEntity handleException(SQLException e) {
		ErrorResponse errResponse = new ErrorResponse();
		errResponse.setResponseStatus(BxConstants.Status.ERROR);
		errResponse.setMessage(BxMessages.Error.SYSTEM_EXCEPTION.getMessage());
		errResponse.setParameters(new String[] { e.getMessage() });
		errResponse.setStackTrace(ExceptionUtils.instance().printException(e));

		logger.debug(ExceptionUtils.instance().printException(e));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
	}
}
