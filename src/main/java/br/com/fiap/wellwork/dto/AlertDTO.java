package br.com.fiap.wellwork.dto;

import br.com.fiap.wellwork.model.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor   // gera construtor com todos os campos
@NoArgsConstructor    // gera construtor vazio (requerido pelo Jackson)
public class AlertDTO {
    private String message;
    private AlertType type;
    private double severity;
    private String recommendation;
}
