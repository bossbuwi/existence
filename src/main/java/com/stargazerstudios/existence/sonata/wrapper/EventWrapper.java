package com.stargazerstudios.existence.sonata.wrapper;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class EventWrapper {
    private long id;
    private String global_prefix;
    private String machine;
    private String[] zones;
    private String[] event_types;
    private String start_date;
    private String end_date;
    private String jira_case;
    private String features_on;
    private String features_off;
    private String compiled_sources;
    private String api_used;
}
