package br.com.fiap.wellwork.repository;

import br.com.fiap.wellwork.model.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {}
