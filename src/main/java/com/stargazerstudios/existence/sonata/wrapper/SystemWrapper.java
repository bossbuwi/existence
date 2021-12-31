package com.stargazerstudios.existence.sonata.wrapper;

import lombok.*;

import java.util.HashMap;

@Getter @Setter @NoArgsConstructor
public class SystemWrapper {
    private HashMap<String, String> system;
    private long id;
    private String global_prefix;
    private String description;
    private String url;
    private String owners;
    private String machine;
}
