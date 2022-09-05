package com.stargazerstudios.existence.sonata.entity;

import com.stargazerstudios.existence.requiem.entity.Coblog;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "sonata_systems")
@Getter @Setter @NoArgsConstructor
public class System {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "global_prefix")
    private String globalPrefix;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "owners")
    private String owners;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp dateCreated;

    @UpdateTimestamp
    @Column(name = "last_changed_date")
    private Timestamp dateChanged;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @OneToMany(mappedBy = "system", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Zone> zones;

    @OneToMany(mappedBy = "system")
    private Set<Event> events;

    @OneToMany(mappedBy = "system")
    private Set<Coblog> coblogs;

    @ManyToOne
    @JoinColumn(name = "release_id")
    private Release release;
}
