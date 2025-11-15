package br.com.fiap.wellwork.controller;

import br.com.fiap.wellwork.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AlertService alertService;
    public AdminController(AlertService alertService){this.alertService = alertService;}

    @GetMapping("/alerts/count")
    public ResponseEntity<?> count(){ return ResponseEntity.ok(alertService.countAlerts()); }
}
