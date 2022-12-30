package com.stargazerstudios.existence.eligius.wrapper;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class EventFilterWrapper {

    private long id;

    private String global_prefix;

    private String machine;

//    private String[] zones;
    private String zone;

//    private String[] event_types;
    private String event_type;

    private String start_date;

    private String end_date;

    private String jira_case;

    private String features_on;

    private String features_off;

    private String compiled_sources;

    private String api_used;

    private String created_by;

    private String last_modified_by;

    private boolean is_imported;
}
