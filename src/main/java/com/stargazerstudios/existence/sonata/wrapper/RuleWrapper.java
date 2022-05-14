package com.stargazerstudios.existence.sonata.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class RuleWrapper {

    @Min(value = 1, groups = PutValidation.class)
    private long id;
    @NotBlank(groups = {
            PostValidation.class, PutValidation.class
    })
    private String body;
}
