package com.danube.danube.model.dto;

import java.util.List;
import java.util.Set;

public record JwtResponse(String jwt, String email, List<String> roles) {
}
