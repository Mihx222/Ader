package com.ader.backend.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generic_gen")
    private Long id;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date updated;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
}
