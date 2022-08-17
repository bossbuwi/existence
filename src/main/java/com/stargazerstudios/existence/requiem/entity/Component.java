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

    // This needs to be rethought. @Lob and columnDefinition are not originally
    // part of this entity and are just added to make it compatible with
    // in memory H2 database for testing and compilation.
    // Once a proper test has been created, these codes should be removed or reworked.
    @Lob
    @Column(name = "details", columnDefinition = "text")
    private String details;

    // See comment on property above.
    @Lob
    @Column(name = "resolution", columnDefinition = "text")
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
