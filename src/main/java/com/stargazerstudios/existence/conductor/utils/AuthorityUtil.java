package com.stargazerstudios.existence.conductor.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityUtil {

    public boolean checkAuthority(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        GrantedAuthority authority = authorities.stream()
                .filter(grantedAuthority ->
                        role.equals(grantedAuthority.getAuthority()))
                .findAny()
                .orElse(null);

        return authority != null;
    }

    public String getAuthUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
