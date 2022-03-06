package com.stargazerstudios.existence.ballad.wrapper;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class TagWrapper {
    private long id;
    private String name;
    private String new_name;
    private String[] stories;
}
