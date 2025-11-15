package br.com.fiap.wellwork.service.impl;

import br.com.fiap.wellwork.model.vo.ComfortIndexVO;
import br.com.fiap.wellwork.model.vo.SensorReadingVO;
import br.com.fiap.wellwork.service.ComfortService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ComfortServiceImpl implements ComfortService {

    // weights must sum to 1.0
    private static final double W_TEMP = 0.25;
    private static final double W_HUM = 0.20;
    private static final double W_LUM = 0.20;
    private static final double W_NOISE = 0.20;
    private static final double W_AIR = 0.15;

    @Override
    public ComfortIndexVO calculate(SensorReadingVO vo) {
        double t = normalizeTemperature(vo.getTemperature());
        double h = normalizeHumidity(vo.getHumidity());
        double l = normalizeLuminosity(vo.getLuminosity());
        double n = normalizeNoise(vo.getNoise());
        double a = normalizeAirQuality(vo.getAirQuality());

        Map<String, Double> factors = new HashMap<>();
        factors.put("temperature", t);
        factors.put("humidity", h);
        factors.put("luminosity", l);
        factors.put("noise", n);
        factors.put("airQuality", a);

        double index = t*W_TEMP + h*W_HUM + l*W_LUM + n*W_NOISE + a*W_AIR;
        index = index * 100.0;

        return new ComfortIndexVO(index, factors);
    }

    private double normalizeTemperature(Double temp) {
        if(temp==null) return 1.0;
        // ideal 21-25 -> 1.0 ; 15 or 30 -> 0.0 linear
        if(temp >=21 && temp <=25) return 1.0;
        if(temp < 15 || temp > 30) return 0.0;
        if(temp < 21) return (temp - 15) / (21 - 15);
        return (30 - temp) / (30 - 25);
    }

    private double normalizeHumidity(Double hum) {
        if(hum==null) return 1.0;
        // ideal 40-60
        if(hum>=40 && hum<=60) return 1.0;
        if(hum < 20 || hum > 80) return 0.0;
        if(hum < 40) return (hum - 20) / (40 - 20);
        return (80 - hum) / (80 - 60);
    }

    private double normalizeLuminosity(Double lux) {
        if(lux==null) return 1.0;
        // ideal 300-500
        if(lux>=300 && lux<=500) return 1.0;
        if(lux < 50) return 0.0;
        if(lux < 300) return (lux - 50) / (300 - 50);
        // above 500 slowly decays to 0 at 2000
        if(lux > 2000) return 0.0;
        return (2000 - lux) / (2000 - 500);
    }

    private double normalizeNoise(Double db) {
        if(db==null) return 1.0;
        // ideal <50 -> 1.0 ; >80 -> 0.0
        if(db <=50) return 1.0;
        if(db >=80) return 0.0;
        return (80 - db) / (80 - 50);
    }

    private double normalizeAirQuality(Double aq) {
        if(aq==null) return 1.0;
        // MQ-135 arbitrary: lower better. define 0-100 good, 100-300 moderate, >300 bad
        if(aq <=100) return 1.0;
        if(aq >=500) return 0.0;
        if(aq <=300) return (300 - aq) / (300 - 100);
        return (500 - aq) / (500 - 300);
    }
}
