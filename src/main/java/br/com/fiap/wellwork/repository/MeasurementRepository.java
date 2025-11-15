package br.com.fiap.wellwork.repository;

import br.com.fiap.wellwork.model.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {}