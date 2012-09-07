package com.comsysto.findparty.web;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 07.09.12
 * Time: 08:24
 * To change this template use File | Settings | File Templates.
 */
public class PartyServiceException extends RuntimeException{

    public PartyServiceException(String message, Throwable ex){
        super(message, ex);
    }

}
