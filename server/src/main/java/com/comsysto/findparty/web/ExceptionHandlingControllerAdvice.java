package com.comsysto.findparty.web;

import com.comsysto.findparty.exceptions.InvalidRequestException;
import com.comsysto.findparty.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User: rpelger
 * Date: 27.11.13
 */
@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    String serverError(RuntimeException ex) {
        return "Server error: " + ex.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody String notFound(ResourceNotFoundException exception) {
        return "Not found: " + exception.getMessage();
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody String badRequest(InvalidRequestException exception) {
        return "Bad request: " + exception.getMessage();
    }

}
