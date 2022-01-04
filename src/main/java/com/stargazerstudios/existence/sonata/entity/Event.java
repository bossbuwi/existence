package com.stargazerstudios.existence.sonata.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sonata_events")
@Getter @Setter @NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_date", columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(name = "end_date", columnDefinition = "DATE")
    private LocalDate endDate;

    @Column(name = "jira_case")
    private String jiraCase;

    @Column(name = "features_on")
    private String featuresOn;

    @Column(name = "features_off")
    private String featuresOff;

    @Column(name = "compiled_sources")
    private String compiledSources;

    @Column(name = "api_used")
    private String apiUsed;

    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp dateCreated;

    @Column(name = "last_changed_by")
    private String lastChangedBy;

    @UpdateTimestamp
    @Column(name = "last_changed_date")
    private Timestamp dateChanged;

    @ManyToOne
    @JoinColumn(name = "system_id")
    private System system;

    @ManyToMany
    @JoinTable(
            name = "events_zones",
            joinColumns = {
                    @JoinColumn(name = "event_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "zone_id", referencedColumnName = "id")
            }
    )
    @OrderBy("id")
    private Set<Zone> zones = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "events_event_types",
            joinColumns = {
                    @JoinColumn(name = "event_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "event_type_id", referencedColumnName = "id")
            }
    )
    @OrderBy("id")
    private Set<EventType> eventTypes = new LinkedHashSet<>();
}
