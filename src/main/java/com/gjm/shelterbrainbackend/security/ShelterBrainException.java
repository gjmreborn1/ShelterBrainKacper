package com.gjm.shelterbrainbackend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ShelterBrainException extends RuntimeException {
    private HttpStatus httpStatus;
    private String responseMessage;
}
