package com.sismics.docs.rest.resource;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Multipart request filter to handle file uploads properly in Jersey 3.x
 * This filter ensures multipart requests are handled correctly
 * 
 * @author lingma
 */
@Provider
@PreMatching
public class MultipartRequestFilter implements ContainerRequestFilter {
    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        String contentType = context.getMediaType() != null ? context.getMediaType().toString() : "";
        if (contentType.startsWith("multipart/")) {
            // Ensure the request is processed as multipart
            context.setProperty("isMultipart", true);
        }
    }
}