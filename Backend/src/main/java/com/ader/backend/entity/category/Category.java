package com.ader.backend.entity.category;

import com.ader.backend.entity.BaseEntity;
import com.ader.backend.entity.offer.Offer;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SequenceGenerator(name = "generic_gen", sequenceName = "category_seq", allocationSize = 1)
public class Category extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @ManyToMany(mappedBy = "categories")
    private List<Offer> offers = new ArrayList<>();
}
