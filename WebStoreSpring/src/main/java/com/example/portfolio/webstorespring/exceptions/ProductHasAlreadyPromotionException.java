package com.example.portfolio.webstorespring.exceptions;

public class ProductHasAlreadyPromotionException extends RuntimeException{

    public ProductHasAlreadyPromotionException() {
        super("This product has already a promotion");
    }
}
