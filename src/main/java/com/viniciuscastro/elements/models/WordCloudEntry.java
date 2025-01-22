package com.viniciuscastro.elements.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "word_cloud_entry")
public class WordCloudEntry {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "word_cloud_id")
    private WordCloud wordCloud;
    @Column(name = "user_id")
    private String userId;
    private String entry;
    @Column(name = "created_at")
    private Date createdAt;
}
