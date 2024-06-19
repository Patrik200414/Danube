package com.danube.danube.model.dto.jwt;

import java.util.List;
import java.util.UUID;

public record JwtResponse(String jwt, String firstName, String lastName, String email, UUID id, List<String> roles) {
}
