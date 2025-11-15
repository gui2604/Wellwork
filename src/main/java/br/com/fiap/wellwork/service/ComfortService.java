package br.com.fiap.wellwork.service;

import br.com.fiap.wellwork.model.vo.ComfortIndexVO;
import br.com.fiap.wellwork.model.vo.SensorReadingVO;

public interface ComfortService {
    ComfortIndexVO calculate(SensorReadingVO vo);
}
