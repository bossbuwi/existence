package com.stargazerstudios.existence.wayfarer.service;

import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DevelopmentServiceImpl implements DevelopmentService {

    @Autowired
    private SystemDAO systemDAO;

    @Override
    public List<SystemDTO> getSystemsWithEvents() {
        List<System> systems = systemDAO.findSystemsWithEvents();
        return null;
    }

}
