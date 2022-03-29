package com.stargazerstudios.existence.sonata.wrapper;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class ZoneWrapper {
    private long id;
    private String zonal_prefix;
    private String zone_name;
    private String new_zonal_prefix;
    private String system;
    private String new_system;
    private String machine;
}
