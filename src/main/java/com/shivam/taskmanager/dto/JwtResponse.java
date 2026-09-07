package com.shivam.taskmanager.dto;

import java.util.Set;

/**
 * Response DTO containing JWT authentication token and user info.
 */
public class JwtResponse {
    private String token;
    private String type;
    private String username;
    private Set<String> roles;

    public JwtResponse(String token, String type, String username, Set<String> roles) {
        this.token = token;
        this.type = type;
        this.username = username;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}