package com.backend.foodproject.service.auth;

import com.backend.foodproject.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    private final boolean enabled;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserInfoDetails(UserInfo userInfo) {
        this.email = userInfo.getEmail();
        this.password = userInfo.getPassword();
        this.enabled = userInfo.isEnabled();
        this.authorities = List.of(new SimpleGrantedAuthority(userInfo.getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
