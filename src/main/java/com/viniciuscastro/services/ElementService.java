package com.viniciuscastro.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
            throw new RuntimeException("Element not found");
        }

        return new ElementResponse(
            element.getId(),
            element.getSlideId(),
            element.getType().name(),
            element.getPositionX(),
            element.getPositionY(),
            element.getWidth(),
            element.getHeight(),
            element.getUserId().equals(this.userService.getUserId())
        );
    }

    @Transactional
    public ElementResponse createElement(String presentationId, String slideId, String elementType) {
        Presentation presentation = this.presentationService.getPresentation(presentationId);
        presentationService.updateLastModified(presentationId);

        Element createdElement = Element.builder()
            .id(UUID.randomUUID().toString())
            .userId(this.userService.getUserId())
            .presentation(presentation)
            .slideId(slideId)
            .type(ElementType.valueOf(elementType))
            .positionX(new Random().nextInt(0, 75))
            .positionY(new Random().nextInt(0, 75))
            .width(40)
            .height(40)
            .build();
        this.repository.persist(createdElement);

        return this.getElement(createdElement.getId());
    }

    @Transactional
    public ElementResponse updateElement(String elementId, int positionX, int positionY, int width, int height) {
        Element element = this.repository.findById(elementId);

        if (element == null) {
            throw new RuntimeException("Element not found");
        }

        presentationService.updateLastModified(element.getPresentation().getId());

        element.setPositionX(positionX);
        element.setPositionY(positionY);
        element.setWidth(width);
        element.setHeight(height);
        this.repository.persist(element);

        return this.getElement(element.getId());
    }

    @Transactional
    public void deleteElement(String elementId) {
        Element element = this.repository.findById(elementId);

        if (element == null) {
            throw new RuntimeException("Element not found");
        }

        presentationService.updateLastModified(element.getPresentation().getId());

        this.repository.delete(element);
    }

    public Map<String, List<ElementResponse>> getElementsFromPresentation(String presentationId) {
        List<Element> elements = this.repository.findByPresentationId(presentationId);

        if (elements == null) {
            return new HashMap<>();
        }

        Map<String, List<ElementResponse>> response = new HashMap<>();

        for (Element element : elements) {
            if (!response.containsKey(element.getSlideId())) {
                response.put(element.getSlideId(), new ArrayList<>());
            }
            List<ElementResponse> pageElements = response.get(element.getSlideId());
            pageElements.add(new ElementResponse(
                element.getId(),
                element.getSlideId(),
                element.getType().name(),
                element.getPositionX(),
                element.getPositionY(),
                element.getWidth(),
                element.getHeight(),
                element.getUserId().equals(this.userService.getUserId())
            ));
        }

        return response;
    }
}
