package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import com.stargazerstudios.existence.symphony.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAccessServiceImpl implements UserAccessService{

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserUtil userUtil;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userDAO.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user: users) {
            UserDTO userDTO = userUtil.wrapUser(user);
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }
}
