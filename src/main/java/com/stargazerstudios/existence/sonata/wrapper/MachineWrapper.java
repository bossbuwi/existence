package com.stargazerstudios.existence.sonata.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class MachineWrapper {
    private long id;
    @NotBlank(groups = {
            PostValidation.class, PutValidation.class
    })
    private String name;
    @NotBlank(groups = PutValidation.class)
    private String new_name;
}
