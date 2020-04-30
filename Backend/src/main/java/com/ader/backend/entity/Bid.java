package com.ader.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bids")
@Data
@SequenceGenerator(name = "generic_gen", sequenceName = "bid_seq", allocationSize = 1)
public class Bid extends BaseEntity {

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Offer offer;
}
