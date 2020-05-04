package com.ader.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@SequenceGenerator(name = "generic_gen", sequenceName = "category_seq", allocationSize = 1)
public class Category extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    private List<Offer> offers = new ArrayList<>();
}
