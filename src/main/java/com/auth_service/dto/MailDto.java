package com.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {

    @NotBlank(message = "Email can't be null or blank.")
    @Email(message = "Incorrect email.")
    @Size(message = "Email size must be in range 6 - 150 symbols.", min = 6, max = 150)
    private String email;

    @NotBlank(message = "Theme can't be null or blank.")
    @Size(message = "Theme size must be in range 4 - 250 symbols.", min = 4, max = 250)
    private String theme;

    @NotBlank(message = "Mail text can't be null or blank.")
    @Size(message = "Minimum text size = 1 symbol.", min = 1)
    private String text;
}
