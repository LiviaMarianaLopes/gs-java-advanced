package br.com.fiap.gs.repository;

import br.com.fiap.gs.model.Cadastro;
import br.com.fiap.gs.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadastroRepository extends JpaRepository<Cadastro, Long> {
    Optional<Cadastro> findByEmail(String email);

}
