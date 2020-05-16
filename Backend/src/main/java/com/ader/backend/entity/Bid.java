package com.ader.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bids")
@Data
@NoArgsConstructor
@SequenceGenerator(name = "generic_gen", sequenceName = "bid_seq", allocationSize = 1)
public class Bid extends BaseEntity {

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Offer offer;

    @OneToOne
    private Persona persona;

    @Column
    private Boolean acceptInitialRequirements;

    @Column
    private Boolean freeProductSample;

    @Column
    private String compensation;
}
