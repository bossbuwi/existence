package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.sonata.dto.RuleDTO;
import com.stargazerstudios.existence.sonata.wrapper.RuleWrapper;

import java.util.List;

public interface RuleService {
    List<RuleDTO> getAllRules();
    RuleDTO createRule(RuleWrapper rule) throws DatabaseException;
    RuleDTO updateRule(RuleWrapper rule) throws AuthorizationException, DatabaseException, EntityException;
    RuleDTO deleteRule(long id) throws AuthorizationException, DatabaseException, EntityException;
}
