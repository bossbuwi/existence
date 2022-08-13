package com.stargazerstudios.existence.requiem.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "requiem_components")
@Getter @Setter @NoArgsConstructor
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sequence")
    private String sequence;

    @Column(name = "details")
    private String details;

    @Column(name = "resolution")
    private String resolution;

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

    @ManyToMany
    @JoinTable(
            name = "coblogs_components",
            joinColumns = {
                    @JoinColumn(name = "component_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "coblog_id", referencedColumnName = "id")
            }
    )
    private Set<Coblog> coblogs = new LinkedHashSet<>();
}
