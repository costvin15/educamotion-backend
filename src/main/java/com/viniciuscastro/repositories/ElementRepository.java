package com.viniciuscastro.repositories;

import java.util.List;

import com.viniciuscastro.models.presentations.Element;
import com.viniciuscastro.models.presentations.ElementType;
import com.viniciuscastro.models.presentations.Presentation;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ElementRepository implements PanacheRepository<Element> {
    @Inject
    PresentationRepository presentationRepository;

    public List<Element> findByPresentationId(String presentationId) {
        Presentation presentation = this.presentationRepository.findById(presentationId)
            .orElseThrow(() -> new IllegalArgumentException("Presentation not found"));
        return find("presentation", presentation)
            .list();
    }

    public Element findById(String elementId) {
        return find("id", elementId)
            .firstResult();
    }

    public List<Element> findByPresentationIdAndTypeAndUser(String presentationId, String userId, ElementType type) {
        Presentation presentation = this.presentationRepository.findById(presentationId)
            .orElseThrow(() -> new IllegalArgumentException("Presentation not found"));
        return find("presentation = ?1 and type = ?2 and userId = ?3", presentation, type, userId)
            .list();
    }
}
