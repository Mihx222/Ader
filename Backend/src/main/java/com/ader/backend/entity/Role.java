package com.ader.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Role {

    @Id
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    private Long id;

    @Column
    private String name;

    @Builder.Default
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();
}
