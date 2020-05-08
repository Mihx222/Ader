package com.ader.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@SequenceGenerator(name = "generic_gen", sequenceName = "offer_seq", allocationSize = 1)
public class Offer extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Timestamp expireDate;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private User author;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private User assignee;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Bid> bids = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "offer_category",
            joinColumns = {
                    @JoinColumn(name = "offer_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "category_id", referencedColumnName = "id")
            }
    )
    private List<Category> categories = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus = OfferStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
}
