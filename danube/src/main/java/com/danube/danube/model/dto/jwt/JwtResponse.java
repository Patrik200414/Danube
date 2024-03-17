package com.danube.danube.model.dto.jwt;

import java.util.List;
import java.util.Set;

public record JwtResponse(String jwt, String firstName, String lastName, String email, long id, List<String> roles) {
}
