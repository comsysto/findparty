package com.comsysto.findparty.security;

import com.comsysto.findparty.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;

/**
 * User: rpelger
 * Date: 06.03.13
 */
public class SimpleUserDetails implements UserDetails {

    private User user;

    public SimpleUserDetails(User user) {
        Assert.notNull(user, "user may not be null!");
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //always not expired -> i.e. 'active'
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //always not locked -> i.e. 'active'
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //always not expired -> i.e. 'active'
    }

    @Override
    public boolean isEnabled() {
        return true; //always enabled -> i.e. 'active'
    }
}
