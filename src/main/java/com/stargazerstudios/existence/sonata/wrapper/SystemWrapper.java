package com.stargazerstudios.existence.sonata.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class SystemWrapper {
    private long id;
    @NotBlank(groups = {
            PostValidation.class
    })
    private String global_prefix;
    private String description;
    private String url;
    private String owners;
    @NotBlank(groups = {
            PostValidation.class
    })
    private String machine;
    private String new_machine;
}
