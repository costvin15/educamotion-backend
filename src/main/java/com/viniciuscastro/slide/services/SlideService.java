package com.viniciuscastro.slide.services;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.viniciuscastro.clients.GoogleSlidesClient;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class SlideService {
    @Inject
    @RestClient
    GoogleSlidesClient slidesClient;
}
