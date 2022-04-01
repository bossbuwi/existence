package com.stargazerstudios.existence.sonata.wrapper;

import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor
public class EventWrapper {

    @Min(value = 1, groups = PutValidation.class)
    private long id;

    @NotBlank(groups = PostValidation.class)
    private String global_prefix;

    @NotBlank(groups = PostValidation.class)
    private String machine;

    @NotEmpty(groups = PostValidation.class)
    private String[] zones;

    @NotEmpty(groups = PostValidation.class)
    private String[] event_types;

    @NotBlank(groups = PostValidation.class)
    private String start_date;

    @NotBlank(groups = PostValidation.class)
    private String end_date;

    private String jira_case;

    private String features_on;

    private String features_off;

    private String compiled_sources;

    private String api_used;
}
