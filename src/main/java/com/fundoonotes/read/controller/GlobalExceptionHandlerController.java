package com.fundoonotes.read.controller;

import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fundoonotes.read.model.ErrorDetails;
import com.fundoonotes.read.util.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandlerController.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleAnyException(Exception ex, WebRequest webRequest) {
		LOGGER.error("PREDEFIND EXCEPTION OCCURED SENDING 500");
		LOGGER.error(ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest webRequest) {
		LOGGER.error("ResourceNotFoundException Occured");
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
	}

}
