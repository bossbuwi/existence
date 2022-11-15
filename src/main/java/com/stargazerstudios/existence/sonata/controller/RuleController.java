package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import com.stargazerstudios.existence.sonata.dto.RuleDTO;
import com.stargazerstudios.existence.sonata.service.RuleServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.RuleWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata")
public class RuleController {

    @Autowired
    private RuleServiceImpl ruleService;

    /* Unguarded Endpoints */
    @GetMapping("/con/rules/index")
    public ResponseEntity<List<RuleDTO>> getAllRules() {
        return new ResponseEntity<>(ruleService.getAllRules(), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @PostMapping("/rules/rule")
    public ResponseEntity<RuleDTO> createRule(@Validated(PostValidation.class)
                                              @RequestBody RuleWrapper rule)
            throws DatabaseException {
        return new ResponseEntity<>(ruleService.createRule(rule), HttpStatus.OK);
    }

    @PutMapping("/rules/rule")
    public ResponseEntity<RuleDTO> updateRule(@Validated(PutValidation.class)
                                                  @RequestBody RuleWrapper rule)
            throws AuthorizationException, DatabaseException, EntityException {
        return new ResponseEntity<>(ruleService.updateRule(rule), HttpStatus.OK);
    }

    @DeleteMapping("/rules/rule/{id}")
    public ResponseEntity<RuleDTO> deleteRule(@NotBlank @PathVariable("id") long id)
            throws AuthorizationException, DatabaseException, EntityException {
        return new ResponseEntity<>(ruleService.deleteRule(id), HttpStatus.OK);
    }
}
