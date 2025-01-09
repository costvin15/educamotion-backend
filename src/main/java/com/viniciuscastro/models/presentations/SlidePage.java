package com.viniciuscastro.models.presentations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "slide_page")
public class SlidePage {
    @Id
    @ManyToOne
    private Presentation presentation;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "slide_id")
    private String slideId;

    @Column(name = "slide_index")
    private Integer slideIndex;

    @Column(name = "stored_filename")
    private String storedFilename;
}
