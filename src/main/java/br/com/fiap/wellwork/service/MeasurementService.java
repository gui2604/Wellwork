package br.com.fiap.wellwork.service;

import br.com.fiap.wellwork.dto.MeasurementDTO;
import br.com.fiap.wellwork.dto.AlertDTO;

import java.util.Optional;

public interface MeasurementService {
    Optional<AlertDTO> process(MeasurementDTO dto, String username);
}
