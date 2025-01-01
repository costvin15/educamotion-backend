package com.viniciuscastro.models.classroom;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "closed_at")
    private Date closedAt;
}
