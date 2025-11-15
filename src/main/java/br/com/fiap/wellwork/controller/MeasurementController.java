package br.com.fiap.wellwork.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.wellwork.dto.AlertDTO;
import br.com.fiap.wellwork.dto.MeasurementDTO;
import br.com.fiap.wellwork.service.MeasurementService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/measurements")
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> receive(@Valid @RequestBody MeasurementDTO dto, Principal principal) {
        Optional<AlertDTO> alert = service.process(dto, principal.getName());

        return alert
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
