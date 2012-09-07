package com.comsysto.findparty;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 07.09.12
 * Time: 09:12
 * To change this template use File | Settings | File Templates.
 */
public class ErrorModel {

    private Throwable throwable;

    public void setThrowable(Throwable ex){
        this.throwable = ex;
    }
    
    public Throwable getThrowable(){
        return throwable;
    }    
    
}
