package com.stargazerstudios.existence.symphony.wrapper;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class UserWrapper {
    private String username;
    private String password;
    private String[] roles;
}
