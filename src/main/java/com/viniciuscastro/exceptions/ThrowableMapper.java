package com.viniciuscastro.exceptions;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ThrowableMapper implements ExceptionMapper<ApplicationException> {
    Logger logger = LoggerFactory.getLogger(ThrowableMapper.class);

    @Override
    public Response toResponse(ApplicationException exception) {
        String errorId = UUID.randomUUID().toString();
        // Devo salvar este erro em uma nova collection do Firestore
        logger.info(errorId);

        ErrorResponse errorResponse = new ErrorResponse(UUID.fromString(errorId), exception.getMessage());
        return Response.status(exception.getStatusCode().getCode()).entity(errorResponse).build();
    }
}
