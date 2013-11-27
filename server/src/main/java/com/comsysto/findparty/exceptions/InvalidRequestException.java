package com.comsysto.findparty.exceptions;

/**
 * User: rpelger
 * Date: 27.11.13
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
