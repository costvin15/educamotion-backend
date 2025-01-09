package com.viniciuscastro.repositories;

import java.util.List;

import com.viniciuscastro.models.presentations.Presentation;
import com.viniciuscastro.models.presentations.SlidePage;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class SlidePageRepository implements PanacheRepository<SlidePage> {
    public List<SlidePage> findByPresentationId(Presentation presentation) {
        return this.list("presentation", presentation);
    }
}
