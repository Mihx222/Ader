package com.ader.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "adv_format")
@Data
@NoArgsConstructor
@SequenceGenerator(name = "generic_gen", sequenceName = "adv_format_seq", allocationSize = 1)
public class AdvertisementFormat extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "advertisementFormats")
    private List<Offer> offer;
}
