package com.viniciuscastro.elements.models;

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
@Entity(name = "word_cloud_element")
public class WordCloud {
    @Id
    private String id;
    private String title;
    @Column(name = "enable_multiple_entries")
    private Boolean enableMultipleEntries;
}
