package com.stargazerstudios.existence.conductor.service;

import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import org.springframework.stereotype.Service;

@Service
public class FrontendService {

    public ExistenceIdentity getAppIdentity() {
        return new ExistenceIdentity();
    }
}
