package br.com.fiap.wellwork.model.entity;

import br.com.fiap.wellwork.model.enums.AlertType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    private String message;
    private Double severity;

    @ManyToOne
    private Measurement measurement;
}
