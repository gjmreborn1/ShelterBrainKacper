package com.gjm.shelterbrainbackend.security;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ShelterBeanValidationException extends ShelterBrainException {
    public ShelterBeanValidationException(BindingResult bindingResult) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "");

         String responseMessage = bindingResult.getFieldErrors()
                 .stream()
                 .map(FieldError::toString)
                 .collect(Collectors.joining("\n"));
         setResponseMessage(responseMessage);
    }
}
