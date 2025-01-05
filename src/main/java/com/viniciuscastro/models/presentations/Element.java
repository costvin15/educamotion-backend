package com.viniciuscastro.models.presentations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name = "element")
@NoArgsConstructor
@AllArgsConstructor
public class Element {
    @Id
    private String id;

    @Id
    @ManyToOne
    private Presentation presentation;

    @Id
    @Column(name = "user_id")
    private String userId;

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
