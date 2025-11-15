package br.com.fiap.wellwork.service.impl;

import br.com.fiap.wellwork.repository.AlertRepository;
import br.com.fiap.wellwork.service.AlertService;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    public AlertServiceImpl(AlertRepository alertRepository){this.alertRepository = alertRepository;}

    @Override
    public long countAlerts(){ return alertRepository.count(); }
}
