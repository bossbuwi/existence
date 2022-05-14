package com.stargazerstudios.existence.sonata.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "sonata_rules")
@Getter @Setter @NoArgsConstructor
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "body")
    private String body;
}
