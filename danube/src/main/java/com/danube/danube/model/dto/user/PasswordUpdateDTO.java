package com.danube.danube.model.dto.user;

public record PasswordUpdateDTO(String currentPassword, String newPassword, String reenterPassword, long userId) {
}
