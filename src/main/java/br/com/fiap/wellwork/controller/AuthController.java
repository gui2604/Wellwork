package br.com.fiap.wellwork.controller;

import br.com.fiap.wellwork.dto.AuthRequestDTO;
import br.com.fiap.wellwork.dto.AuthResponseDTO;
import br.com.fiap.wellwork.model.entity.User;
import br.com.fiap.wellwork.repository.UserRepository;
import br.com.fiap.wellwork.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Set;
import br.com.fiap.wellwork.model.enums.Role;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO req){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = jwtUtil.generateToken(req.getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    // Simple register endpoint for testing (creates user with ROLE_USER)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequestDTO req){
        if(userRepository.findByUsername(req.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("User exists");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRoles(Set.of(Role.ROLE_USER));
        userRepository.save(u);
        String token = jwtUtil.generateToken(u.getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
