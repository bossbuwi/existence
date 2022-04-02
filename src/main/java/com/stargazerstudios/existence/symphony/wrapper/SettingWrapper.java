package com.stargazerstudios.existence.symphony.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class SettingWrapper {
    private long id;
    @NotBlank(groups = PutValidation.class)
    private String key;
    @NotBlank(groups = PutValidation.class)
    private String value;
}
