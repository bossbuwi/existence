package com.stargazerstudios.existence.ballad.wrapper;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class StoryWrapper {
    private long id;
    private String name;
    private String new_name;
    private String[] tags;
}
