package com.viniciuscastro.services;

import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class UserService {
    @Inject
    JsonWebToken jwt;

    public String getUserId() {
        return jwt.getSubject();
    }
}
