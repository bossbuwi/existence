package com.stargazerstudios.existence.requiem.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class CoblogWrapper {

    @Min(value = 1, groups = PutValidation.class)
    private long id;
    @NotBlank(groups = PostValidation.class)
    private String description;
    @NotBlank(groups = PostValidation.class)
    private String run_day;
    @NotBlank(groups = PostValidation.class)
    private String next_run_day;
    @NotBlank(groups = PostValidation.class)
    private String conclusion;
    @NotBlank(groups = PostValidation.class)
    private boolean open;
    @Min(value = 1, groups = PostValidation.class)
    private long zone_id;
}
