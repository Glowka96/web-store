package com.example.portfolio.webstorespring.exceptions;

public class NotFoundSubcategoriesByNamesException extends RuntimeException {
    public NotFoundSubcategoriesByNamesException() {
        super("Subcategories not found for the provided names");
    }
}
