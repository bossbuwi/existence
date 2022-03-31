package com.stargazerstudios.existence.symphony.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.GetValidation;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class RoleWrapper {
    @NotBlank(groups = GetValidation.class)
    private String name;
}
