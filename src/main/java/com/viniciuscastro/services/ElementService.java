package com.viniciuscastro.services;

import java.util.ArrayList;
import java.util.List;

import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.models.presentations.Element;
import com.viniciuscastro.repositories.ElementRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ElementService {
    @Inject
    ElementRepository repository;

    public List<ElementResponse> getElementsFromPresentation(String presentationId) {
        List<Element> elements = this.repository.findByPresentationId(presentationId);

        if (elements == null) {
            return new ArrayList<>();
        }

        List<ElementResponse> response = new ArrayList<>();

        for (Element element : elements) {
            response.add(new ElementResponse(
                element.getId(),
                element.getSlideId(),
                element.getType().name(),
                element.getPositionX(),
                element.getPositionY(),
                element.getWidth(),
                element.getHeight()
            ));
        }

        return response;
    }
}
