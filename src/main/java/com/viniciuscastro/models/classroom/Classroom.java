package com.viniciuscastro.models.classroom;

import java.time.Instant;
import java.util.Date;

import com.viniciuscastro.models.presentations.Presentation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "classroom")
public class Classroom {
    @Id
    private String id;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @ManyToOne
    private Presentation presentation;

    @Id
    private Boolean active;

    @Column(name = "current_slide")
    private String currentSlide;

    @Column(name = "entry_code", unique = true)
    private String entryCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "closed_at")
    private Date closedAt;

    public Classroom(String id, String userId, String entryCode, Presentation presentation) {
        this.id = id;
        this.userId = userId;
        this.entryCode = entryCode;
        this.presentation = presentation;
        this.active = true;
        this.createdAt = Date.from(Instant.now());
        this.updatedAt = Date.from(Instant.now());
        this.closedAt = null;
    }
}
