package com.comsysto.findparty;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * com.comsysto.findparty.User: tim.hoheisel
 * Date: 05.09.12
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
public class Picture implements Serializable{

    private String id;

    private byte[] content;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
