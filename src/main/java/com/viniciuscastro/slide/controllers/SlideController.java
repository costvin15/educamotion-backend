package com.viniciuscastro.slide.controllers;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.Path;

@Path("slide")
@Authenticated
public class SlideController {
};