package com.ader.backend.entity.persona;


import com.ader.backend.entity.BaseEntity;
import com.ader.backend.entity.user.User;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "personas")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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
