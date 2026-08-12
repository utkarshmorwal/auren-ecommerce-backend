package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "roles", user.getRoles()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = userRepository.findAll().stream()
                .map(u -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", u.getId());
                    map.put("name", u.getName());
                    map.put("email", u.getEmail());
                    map.put("roles", u.getRoles());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    public static class RoleUpdateRequest {
        public boolean makeAdmin;
    }

    @PutMapping("/admin/{id}/role")
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @PathVariable Long id,
            @RequestBody RoleUpdateRequest request,
            Authentication authentication) {

        User target = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User requester = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (requester.getId().equals(target.getId()) && !request.makeAdmin) {
            throw new RuntimeException("You cannot remove your own admin access");
        }

        Set<String> roles = new HashSet<>(target.getRoles());
        if (request.makeAdmin) {
            roles.add("ADMIN");
        } else {
            roles.remove("ADMIN");
        }
        target.setRoles(roles);
        userRepository.save(target);

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("id", target.getId());
        response.put("name", target.getName());
        response.put("email", target.getEmail());
        response.put("roles", target.getRoles());
        return ResponseEntity.ok(response);
    }
}