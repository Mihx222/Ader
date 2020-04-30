package com.ader.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@Data
public class BaseEntity implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generic_gen")
    private Long id;

    @CreationTimestamp
    private Timestamp created;

    @UpdateTimestamp
    private Timestamp updated;
}
