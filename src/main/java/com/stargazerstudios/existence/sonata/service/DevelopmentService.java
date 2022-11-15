package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.sonata.repository.EventDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DevelopmentService {

    @Autowired
    private AuthorityUtil authorityUtil;

    @Autowired
    private EventDAO eventDAO;

    public Boolean deleteAllEvents() throws AuthorizationException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.OWNER.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        eventDAO.deleteAll();
        return true;
    }
}
