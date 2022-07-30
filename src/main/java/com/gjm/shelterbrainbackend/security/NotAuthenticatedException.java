package com.gjm.shelterbrainbackend.security;

import org.springframework.http.HttpStatus;

public class NotAuthenticatedException extends ShelterBrainException {
    public NotAuthenticatedException() {
        super(HttpStatus.UNAUTHORIZED, "JWT security violation!");
    }
}
