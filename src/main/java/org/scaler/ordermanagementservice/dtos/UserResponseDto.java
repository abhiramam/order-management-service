package org.scaler.ordermanagementservice.dtos;

import java.util.List;

public record UserResponseDto(String id, String username, List<String> roles) {
}
