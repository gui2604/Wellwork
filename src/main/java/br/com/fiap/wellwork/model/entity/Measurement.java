package br.com.fiap.wellwork.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private Double temperature;
    private Double humidity;
    private Double luminosity;
    private Double noise;
    private Double airQuality;
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
