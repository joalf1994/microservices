package com.jbobadilla.inventory_service.model.dto;

/**
 * User: jbobadilla
 * Date: 4/03/2026
 * Time: 02:08
 */
public record Response(String[] errorsMessages) {

    public boolean hasErrors() {
        return errorsMessages != null && errorsMessages.length > 0;
    }
}