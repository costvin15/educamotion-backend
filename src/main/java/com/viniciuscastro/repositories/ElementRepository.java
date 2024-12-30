package com.viniciuscastro.repositories;

import java.util.List;

import com.viniciuscastro.models.presentations.Element;
import com.viniciuscastro.models.presentations.Presentation;
import com.viniciuscastro.services.UserService;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ElementRepository implements PanacheRepository<Element> {
    @Inject
    PresentationRepository presentationRepository;

    @Inject
    UserService userService;

    public List<Element> findByPresentationId(String presentationId) {
        Presentation presentation = this.presentationRepository.findById(presentationId, this.userService.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Presentation not found"));
        return find("presentation", presentation)
            .list();
    }
}
