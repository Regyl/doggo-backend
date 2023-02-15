package ru.doggo.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CredentialsResponse {

    private String token;

    private String refreshToken;

    private long expiresIn;
}
