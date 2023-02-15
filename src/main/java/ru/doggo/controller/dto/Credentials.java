package ru.doggo.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Credentials {

    @NotNull
    private String login;

    @NotNull
    private String password;
}
