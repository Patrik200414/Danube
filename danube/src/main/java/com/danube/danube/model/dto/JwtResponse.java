package com.danube.danube.model.dto;

import java.util.Set;

public record JwtResponse(String jwt, String email, Set<String> roles) {
}
