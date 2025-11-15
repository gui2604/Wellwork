package br.com.fiap.wellwork.model.vo;

import lombok.Value;
import java.util.Map;

@Value
public class ComfortIndexVO {
    double index;
    Map<String, Double> factors;
}
