package br.com.fiap.wellwork.model.vo;

import lombok.Value;

@Value
public class SensorReadingVO {
    Double temperature;
    Double humidity;
    Double luminosity;
    Double noise;
    Double airQuality;
}
