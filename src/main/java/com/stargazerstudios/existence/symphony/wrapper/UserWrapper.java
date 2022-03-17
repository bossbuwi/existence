package com.stargazerstudios.existence.symphony.wrapper;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class UserWrapper {
    private String username;
    private String password;
    private String old_password;
    private String new_password;
    private String confirm_password;
    private String[] roles;
}
