package ru.doggo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenDto {

    @NotBlank
    private String refreshToken;
}
