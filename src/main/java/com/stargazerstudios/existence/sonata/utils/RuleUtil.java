package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.dto.RuleDTO;
import com.stargazerstudios.existence.sonata.entity.Rule;
import org.springframework.stereotype.Service;

@Service
public class RuleUtil {

    public RuleDTO wrapRule(Rule rule) {
        RuleDTO ruleDTO = new RuleDTO();
        ruleDTO.setId(rule.getId());
        ruleDTO.setBody(rule.getBody());
        return ruleDTO;
    }
}
