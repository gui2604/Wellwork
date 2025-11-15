package br.com.fiap.wellwork.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeasurementDTO {

    @NotNull
    private String timestamp; // ISO 8601

    private Double temperature;
    private Double humidity;
    private Double luminosity;
    private Double noise;
    private Double airQuality;
    private String location;
}
