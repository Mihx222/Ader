package com.ader.backend.entity.bid;

import com.ader.backend.entity.BaseEntity;
import com.ader.backend.entity.offer.Offer;
import com.ader.backend.entity.user.User;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bids")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SequenceGenerator(name = "generic_gen", sequenceName = "bid_seq", allocationSize = 1)
public class Bid extends BaseEntity {

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Offer offer;
}
