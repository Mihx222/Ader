package com.ader.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SequenceGenerator(name = "generic_gen", sequenceName = "user_seq", allocationSize = 1)
public class User extends BaseEntity {

    @Column(unique = true)
    private String brandName;

    @Column(unique = true)
    private String brandWebsite;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id")
            }
    )
    private List<Role> roles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Offer> createdOffers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private List<Offer> assignedOffers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Persona> personas = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
}
