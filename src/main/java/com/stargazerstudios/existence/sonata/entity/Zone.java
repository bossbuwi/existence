package com.stargazerstudios.existence.sonata.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "sonata_zones")
@Getter @Setter @NoArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "zonal_prefix")
    private String zonalPrefix;

    @Column(name = "zone_name")
    private String zoneName;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp dateCreated;

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
                    @JoinColumn(name = "zone_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "event_id", referencedColumnName = "id")
            }
    )
    private Set<Event> events;
}
