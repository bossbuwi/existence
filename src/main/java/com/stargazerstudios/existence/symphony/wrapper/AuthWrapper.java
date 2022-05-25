package com.stargazerstudios.existence.symphony.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor
public class AuthWrapper {
    @NotBlank(groups = {
            PostValidation.class, GetValidation.class, PutValidation.class,
            PutRelationValidation.class, AuthValidation.class, DeleteValidation.class
    })
    private String username;
    @NotBlank(groups = PostValidation.class)
    private String password;
    @NotBlank(groups = PutValidation.class)
    private String old_password;
    @NotBlank(groups = PutValidation.class)
    private String new_password;
    @NotBlank(groups = PutValidation.class)
    private String confirm_password;
    @NotEmpty(groups = PutRelationValidation.class)
    private String[] roles;
}
