package com.stargazerstudios.existence.sonata.constants;

public class ConsSonataConstraint {
    public static final String UNIQUE_MACHINE_NAME = "ct_unique_machine_name";
    public static final String UNIQUE_SYSTEM_PER_MACHINE = "ct_unique_system_per_machine";
    public static final String UNIQUE_ZONE_PER_SYSTEM = "ct_unique_zone_per_system";
    public static final String UNIQUE_RELEASE_NAME = "ct_unique_release_name";
    public static final String SYSTEM_DEPENDS_ON_MACHINE = "fk_machine_id";
    public static final String EVENT_DEPENDS_ON_SYSTEM = "fk_system_id_on_events";
}
