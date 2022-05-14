package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.sonata.dto.RuleDTO;
import com.stargazerstudios.existence.sonata.wrapper.RuleWrapper;

import java.util.List;

public interface RuleService {
    List<RuleDTO> getAllRules();
    RuleDTO createRule(RuleWrapper rule) throws DatabaseErrorException;
    RuleDTO updateRule(RuleWrapper rule) throws AuthorizationErrorException, DatabaseErrorException, EntityErrorException;
    RuleDTO deleteRule(long id) throws AuthorizationErrorException,DatabaseErrorException, EntityErrorException;
}
