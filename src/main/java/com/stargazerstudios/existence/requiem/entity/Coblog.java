package com.stargazerstudios.existence.requiem.entity;

import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "requiem_coblogs")
@Getter @Setter @NoArgsConstructor
public class Coblog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "run_day")
    private LocalDate runDay;

    @Column(name = "next_run_day")
    private LocalDate nextRunDay;

    @Column(name = "open")
    private boolean open;

    @Column(name = "active_user")
    private String activeUser;

    @Column(name = "conclusion")
    private String conclusion;

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

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @ManyToMany
    @JoinTable(
            name = "coblogs_components",
            joinColumns = {
                    @JoinColumn(name = "coblog_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "component_id", referencedColumnName = "id")
            }
    )
    private Set<Component> components = new LinkedHashSet<>();
}
