package com.comsysto.dalli.android.model;

import android.accounts.Account;

/**
 * Created with IntelliJ IDEA.
 * User: rpelger
 * Date: 21.11.12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class UserAccount extends Account {

    private String username;

    private String password;

    public UserAccount(String name, String password, String type) {
        super(name, type);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
