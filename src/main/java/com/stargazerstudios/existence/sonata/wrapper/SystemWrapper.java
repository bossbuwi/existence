package com.stargazerstudios.existence.sonata.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostFullValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor
public class SystemWrapper {

    @Min(value = 1, groups = PutValidation.class)
    private long id;
    @NotBlank(groups = {
            PostValidation.class, PutValidation.class, PostFullValidation.class
    })
    private String global_prefix;
    @NotBlank(groups = {
            PostValidation.class, PutValidation.class, PostFullValidation.class
    })
    private String release;
    private String description;
    private String url;
    private String owners;
    @NotBlank(groups = {
            PostValidation.class, PutValidation.class, PostFullValidation.class
    })
    private String machine;

    @NotEmpty(groups = PostFullValidation.class)
    private ZoneWrapper[] zones;
}
