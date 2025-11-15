package br.com.fiap.wellwork.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.wellwork.dto.AlertDTO;
import br.com.fiap.wellwork.dto.MeasurementDTO;
import br.com.fiap.wellwork.model.entity.Alert;
import br.com.fiap.wellwork.model.entity.Measurement;
import br.com.fiap.wellwork.model.enums.AlertType;
import br.com.fiap.wellwork.model.vo.ComfortIndexVO;
import br.com.fiap.wellwork.model.vo.SensorReadingVO;
import br.com.fiap.wellwork.repository.AlertRepository;
import br.com.fiap.wellwork.repository.MeasurementRepository;
import br.com.fiap.wellwork.repository.UserRepository;
import br.com.fiap.wellwork.service.ComfortService;
import br.com.fiap.wellwork.service.MeasurementService;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final ComfortService comfortService;
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository,
                                  ComfortService comfortService,
                                  AlertRepository alertRepository,
                                  UserRepository userRepository) {
        this.measurementRepository = measurementRepository;
        this.comfortService = comfortService;
        this.alertRepository = alertRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<AlertDTO> process(MeasurementDTO dto, String username) {
        Measurement m = new Measurement();
        try {
            if(dto.getTimestamp()!=null) {
                m.setTimestamp(LocalDateTime.parse(dto.getTimestamp()));
            } else {
                m.setTimestamp(LocalDateTime.now());
            }
        } catch (DateTimeParseException ex) {
            m.setTimestamp(LocalDateTime.now());
        }
        m.setTemperature(dto.getTemperature());
        m.setHumidity(dto.getHumidity());
        m.setLuminosity(dto.getLuminosity());
        m.setNoise(dto.getNoise());
        m.setAirQuality(dto.getAirQuality());
        m.setLocation(dto.getLocation());

        if(username != null) {
            userRepository.findByUsername(username).ifPresent(m::setUser);
        }
        measurementRepository.save(m);

        SensorReadingVO vo = new SensorReadingVO(m.getTemperature(), m.getHumidity(), m.getLuminosity(), m.getNoise(), m.getAirQuality());
        ComfortIndexVO comfort = comfortService.calculate(vo);

        double index = comfort.getIndex();

        if(index >= 75.0) {
            return Optional.empty();
        }

        // find worst factor
        Map<String, Double> factors = comfort.getFactors();
        String worst = factors.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue)).map(Map.Entry::getKey).orElse("unknown");
        AlertType type = mapToAlertType(worst);

        String message = buildMessage(type, m);
        double severity = (75.0 - index) / 75.0; // normalized severity
        String recommendation = recommendationFor(type);

        Alert alert = new Alert();
        alert.setCreatedAt(LocalDateTime.now());
        alert.setType(type);
        alert.setMessage(message);
        alert.setSeverity(severity);
        alert.setMeasurement(m);
        alertRepository.save(alert);

        AlertDTO dtoOut = new AlertDTO(message, type, severity, recommendation);
        return Optional.of(dtoOut);
    }

    private AlertType mapToAlertType(String key) {
        return switch (key) {
            case "temperature" -> AlertType.TEMPERATURE;
            case "humidity" -> AlertType.HUMIDITY;
            case "luminosity" -> AlertType.LUMINOSITY;
            case "noise" -> AlertType.NOISE;
            case "airQuality" -> AlertType.AIR_QUALITY;
            default -> AlertType.MULTIPLE;
        };
    }

    private String buildMessage(AlertType type, Measurement m) {
        return switch (type) {
            case TEMPERATURE -> "Temperatura " + m.getTemperature() + "°C está fora da faixa ideal.";
            case HUMIDITY -> "Umidade " + m.getHumidity() + "% está fora da faixa ideal.";
            case LUMINOSITY -> "Luminosidade " + m.getLuminosity() + " lux está fora da faixa ideal.";
            case NOISE -> "Nível de ruído " + m.getNoise() + " dB está alto.";
            case AIR_QUALITY -> "Índice de qualidade do ar " + m.getAirQuality() + " indica ar ruim.";
            default -> "Múltiplos sensores indicando desconforto.";
        };
    }

    private String recommendationFor(AlertType type) {
        return switch (type) {
            case TEMPERATURE -> "Ajuste o ar-condicionado ou abra a janela.";
            case HUMIDITY -> "Considere usar umidificador/desumidificador ou abrir a janela.";
            case LUMINOSITY -> "Ajuste a iluminação local ou reposicione a estação de trabalho.";
            case NOISE -> "Use fones com cancelamento ou reduza fontes de ruído.";
            case AIR_QUALITY -> "Abra janelas ou verifique ventilação; considere plantas filtrantes.";
            default -> "Verifique os parâmetros do ambiente e faça ajustes.";
        };
    }
}
