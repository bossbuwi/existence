package com.stargazerstudios.existence.symphony.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "uni_settings")
@Getter @Setter @NoArgsConstructor
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @Column(name = "length")
    private long length;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "valid_values")
    private String validValues;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "changed_by")
    private String changedBy;

    @UpdateTimestamp
    @Column(name = "last_changed_date")
    private Timestamp dateChanged;
}
