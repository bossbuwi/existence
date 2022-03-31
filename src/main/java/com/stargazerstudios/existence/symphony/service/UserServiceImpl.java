package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import com.stargazerstudios.existence.symphony.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "UserServiceImpl")
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserUtil userUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> _userData = userDAO.findByUsername(username);
        if (_userData.isPresent()) {
            User _user = _userData.get();
            return new org.springframework.security.core.userdetails.User(
                    _user.getUsername(), _user.getPassword(), getAuthority(_user)
            );
        } else {
            throw new UsernameNotFoundException("User with username: " + username + " not found.");
        }
    }

    private Collection<SimpleGrantedAuthority> getAuthority(User user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return authorities;
    }
}
