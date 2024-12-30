package com.viniciuscastro.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.models.presentations.Element;
import com.viniciuscastro.models.presentations.ElementType;
import com.viniciuscastro.models.presentations.Presentation;
import com.viniciuscastro.repositories.ElementRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class ElementService {
    @Inject
    PresentationService presentationService;

    @Inject
    UserService userService;

    @Inject
    ElementRepository repository;

    public ElementResponse getElement(String elementId) {
        Element element = this.repository.findById(elementId);

        if (element == null) {
            return null;
        }

        return new ElementResponse(
            element.getId(),
            element.getSlideId(),
            element.getType().name(),
            element.getPositionX(),
            element.getPositionY(),
            element.getWidth(),
            element.getHeight()
        );
    }

    @Transactional
    public ElementResponse createElement(String presentationId, String slideId, String elementType) {
        Presentation presentation = this.presentationService.getPresentation(presentationId);

        Element createdElement = Element.builder()
            .id(UUID.randomUUID().toString())
            .userId(this.userService.getUserId())
            .presentation(presentation)
            .slideId(slideId)
            .type(ElementType.valueOf(elementType))
            .positionX(0)
            .positionY(0)
            .width(0)
            .height(0)
            .build();
        this.repository.persist(createdElement);

        return this.getElement(createdElement.getId());
    }

    public Map<String, ElementResponse> getElementsFromPresentation(String presentationId) {
        List<Element> elements = this.repository.findByPresentationId(presentationId);

        if (elements == null) {
            return new HashMap<>();
        }

        Map<String, ElementResponse> response = new HashMap<>();

        for (Element element : elements) {
            response.put(element.getSlideId(), new ElementResponse(
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
