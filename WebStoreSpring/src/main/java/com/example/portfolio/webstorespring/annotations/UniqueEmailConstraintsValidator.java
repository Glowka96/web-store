package com.example.portfolio.webstorespring.annotations;

import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailConstraintsValidator implements ConstraintValidator<UniqueEmail, String> {

    private final AccountRepository accountRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !accountRepository.existsByEmail(email);
    }
}
