package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.EntityDeletionException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.sonata.dto.RuleDTO;
import com.stargazerstudios.existence.sonata.entity.Rule;
import com.stargazerstudios.existence.sonata.repository.RuleDAO;
import com.stargazerstudios.existence.sonata.utils.RuleUtil;
import com.stargazerstudios.existence.sonata.wrapper.RuleWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleDAO ruleDAO;

    @Autowired
    private RuleUtil ruleUtil;

    @Autowired
    private AuthorityUtil authUtil;

    @Override
    public List<RuleDTO> getAllRules() {
        List<RuleDTO> ruleList = new ArrayList<>();
        List<Rule> rules = ruleDAO.findAll();
        if (!rules.isEmpty()) {
            for (Rule rule: rules) {
                ruleList.add(ruleUtil.wrapRule(rule));
            }
        }
        return ruleList;
    }

    @Override
    public RuleDTO createRule(RuleWrapper wRule) throws DatabaseException {
        Rule rule = new Rule();
        rule.setBody(wRule.getBody());

        try {
            ruleDAO.save(rule);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveException("rule");
        }

        return ruleUtil.wrapRule(rule);
    }

    @Override
    public RuleDTO updateRule(RuleWrapper wRule)
            throws AuthorizationException, DatabaseException, EntityException {
        boolean isAuth = authUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isAuth) throw new UserUnauthorizedException();

        Optional<Rule> ruleData = ruleDAO.findById(wRule.getId());
        if (ruleData.isEmpty()) throw new EntityNotFoundException("rule", "id", Long.toString(wRule.getId()));

        Rule rule = ruleData.get();
        rule.setBody(wRule.getBody());

        try {
            ruleDAO.save(rule);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveException("rule");
        }

        return ruleUtil.wrapRule(rule);
    }

    @Override
    public RuleDTO deleteRule(long id)
            throws AuthorizationException, DatabaseException, EntityException {
        boolean isAuth = authUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isAuth) throw new UserUnauthorizedException();

        Optional<Rule> ruleData = ruleDAO.findById(id);
        if (ruleData.isEmpty()) throw new EntityNotFoundException("rule", "id", Long.toString(id));

        Rule rule = ruleData.get();

        try {
            ruleDAO.delete(rule);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityDeletionException("rule");
        }

        return ruleUtil.wrapRule(rule);
    }
}
