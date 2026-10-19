package org.scaler.ordermanagementservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateUserRequestDto(
        @NotBlank @Size(min = 3, max = 64) String username,
        @NotBlank @Size(min = 8) String password,
        @NotEmpty List<@NotBlank @Pattern(regexp = "(?i)ADMIN|USER") String> roles) {
}
