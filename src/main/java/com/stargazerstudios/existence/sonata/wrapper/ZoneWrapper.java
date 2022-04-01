package com.stargazerstudios.existence.sonata.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class ZoneWrapper {
    private long id;

    @NotBlank(groups = PostValidation.class)
    private String zonal_prefix;

    @NotBlank(groups = PostValidation.class)
    private String zone_name;

    private String new_zonal_prefix;

    @NotBlank(groups = PostValidation.class)
    private String system;

    private String new_system;

    @NotBlank(groups = PostValidation.class)
    private String machine;
}
