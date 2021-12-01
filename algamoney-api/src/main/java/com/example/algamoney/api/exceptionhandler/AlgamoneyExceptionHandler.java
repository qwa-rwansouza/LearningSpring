package com.example.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String messageUser = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String messageDev = ex.getCause().toString();
		
		List<Error> errors = Arrays.asList(new Error(messageUser, messageDev));
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		
		List<Error> errors = createListError(ex.getBindingResult());
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Error> createListError(BindingResult bindingResult) {
		List<Error> errors = new ArrayList<>();
		
		for(FieldError fieldError : bindingResult.getFieldErrors()) {
			String messageUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String messageDevString = fieldError.toString();
			
			errors.add(new Error(messageUser, messageDevString));
		}
		
		return errors;
	}
	
	public static class Error {
		private String messageUser;
		private String messageDev;
		
		public Error(String messageUser, String messageDev) {
			this.messageUser = messageUser;
			this.messageDev = messageDev;
		}

		public String getMessageUser() {
			return messageUser;
		}

		public String getMessageDev() {
			return messageDev;
		}		
		
	}
}
