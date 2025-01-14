package com.viniciuscastro.models.presentations;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "presentation")
public class Presentation {
    @Id
    private String id;

    private String title;

    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "last_modified")
    private Date lastModified;
}
