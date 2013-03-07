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

    private String password;

    public UserAccount(String name, String password, String type) {
        super(name, type);
        this.password = password;
    }

    public String getUsername() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
