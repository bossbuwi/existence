package com.stargazerstudios.existence.ballad.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor
public class StoryWrapper {
    private long id;
    @NotBlank(groups = {
            PostValidation.class, GetValidation.class, PutValidation.class,
            PutRelationValidation.class, DeleteValidation.class
    })
    private String name;
    @NotBlank(groups = PutValidation.class)
    private String new_name;
    @NotEmpty(groups = PutRelationValidation.class)
    private String[] tags;
}
