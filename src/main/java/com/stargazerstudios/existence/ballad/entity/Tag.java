package com.stargazerstudios.existence.ballad.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ballad_tags")
@Getter @Setter @NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp dateCreated;

    @UpdateTimestamp
    @Column(name = "last_changed_date")
    private Timestamp dateChanged;

    @ManyToMany
    @JoinTable(
            name = "stories_tags",
            joinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "story_id", referencedColumnName = "id")
            }
    )
    @OrderBy("id")
    private Set<Story> stories = new LinkedHashSet<>();
}
