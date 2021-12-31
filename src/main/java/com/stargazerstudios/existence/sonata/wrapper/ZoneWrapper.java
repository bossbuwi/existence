package com.stargazerstudios.existence.sonata.wrapper;

import lombok.*;

import java.util.HashMap;

@Getter @Setter @NoArgsConstructor
public class ZoneWrapper {
    private HashMap<String, String> zone;
    private long id;
    private String zonal_prefix;
    private String zone_name;
    private String system;
}
