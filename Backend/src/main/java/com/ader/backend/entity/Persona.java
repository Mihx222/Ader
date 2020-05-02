package com.ader.backend.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "personas")
@Data
@NoArgsConstructor
@SequenceGenerator(name = "generic_gen", sequenceName = "persona_seq", allocationSize = 1)
public class Persona extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String activity;

    @Column(nullable = false)
    private String audience;

    @Column(nullable = false)
    private String sellingOrientation;

    @Column(nullable = false)
    private String values;
}
