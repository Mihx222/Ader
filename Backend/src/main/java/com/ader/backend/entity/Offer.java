package com.ader.backend.entity;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "offers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SequenceGenerator(name = "generic_gen", sequenceName = "offer_seq", allocationSize = 1)
public class Offer extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn
    private User author;
}
