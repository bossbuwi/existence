package com.stargazerstudios.existence.sonata.wrapper;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class SystemWrapper {
    private long id;
    private String global_prefix;
    private String description;
    private String url;
    private String owners;
    private String machine;
    private String new_machine;
}
