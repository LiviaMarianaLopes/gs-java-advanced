package br.com.fiap.gs.repository;

import br.com.fiap.gs.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRepository extends JpaRepository<Login, Long> {
}
