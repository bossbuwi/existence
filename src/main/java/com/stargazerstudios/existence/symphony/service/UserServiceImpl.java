package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "UserServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isPresent()) {
            User _user = userData.get();
            return new org.springframework.security.core.userdetails.User(
                    _user.getUsername(),
                    _user.getPassword(),
                    getAuthority(_user));
        } else {
            throw new UsernameNotFoundException("User with username: " + username + " not found.");
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userDAO.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user: users) {
            UserDTO userDTO = new UserDTO(user);
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    private Collection<SimpleGrantedAuthority> getAuthority(User user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return authorities;
    }
}
