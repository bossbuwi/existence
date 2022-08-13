package com.stargazerstudios.existence.requiem.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class ComponentWrapper {

    @Min(value = 1, groups = PutValidation.class)
    private long id;
    @NotBlank(groups = PostValidation.class)
    private String name;
    @NotBlank(groups = PostValidation.class)
    private String sequence;
    @NotBlank(groups = PostValidation.class)
    private String details;
    @NotBlank(groups = PostValidation.class)
    private String resolution;
    @Min(value = 1, groups = PostValidation.class)
    private long coblog_id;
}
