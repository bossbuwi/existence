package com.stargazerstudios.existence.sonata.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "sonata_event_types")
@Getter @Setter @NoArgsConstructor
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "exclusive")
    private boolean exclusive;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp dateAdded;

    @UpdateTimestamp
    @Column(name = "last_changed_date")
    private Timestamp dateChanged;

    @ManyToMany
    @JoinTable(
            name = "events_event_types",
            joinColumns = {
                    @JoinColumn(name = "event_type_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "event_id", referencedColumnName = "id")
            }
    )
    private Set<Event> events;
}
