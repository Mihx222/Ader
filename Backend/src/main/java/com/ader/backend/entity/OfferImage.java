package com.ader.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "offerimage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "generic_gen", sequenceName = "offerimage_seq", allocationSize = 1)
public class OfferImage extends BaseEntity {

    @ManyToOne
    @JoinColumn
    private Offer offer;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Column(name = "pic_byte", length = 1000)
    private byte[] picByte;

    public OfferImage(String name, String type, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }
}
