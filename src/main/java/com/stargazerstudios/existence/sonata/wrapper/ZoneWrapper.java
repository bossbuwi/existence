package com.stargazerstudios.existence.sonata.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostFullValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class ZoneWrapper {
    private long id;

    @NotBlank(groups = {
            PostValidation.class, PutValidation.class, PostFullValidation.class
    })
    private String zonal_prefix;

    @NotBlank(groups = {
            PostValidation.class, PutValidation.class, PostFullValidation.class
    })
    private String zone_name;

    private String new_zonal_prefix;

    @NotBlank(groups = PostValidation.class)
    private String system;

    private String new_system;

    @NotBlank(groups = PostValidation.class)
    private String machine;
}
