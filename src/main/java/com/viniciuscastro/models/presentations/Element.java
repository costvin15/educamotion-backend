package com.viniciuscastro.models.presentations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity(name = "element")
public class Element {
    @Id
    private String id;

    @Id
    @ManyToOne
    private Presentation presentation;

    @Id
    @Column(name = "slide_id")
    private String slideId;

    @Enumerated(EnumType.STRING)
    private ElementType type;

    private Integer positionX;

    private Integer positionY;

    private Integer width;

    private Integer height;
}
