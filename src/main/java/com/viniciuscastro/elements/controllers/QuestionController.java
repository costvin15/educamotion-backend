package com.viniciuscastro.elements.controllers;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.Path;

@Authenticated
@Path("element/question")
public class QuestionController {
        
}
