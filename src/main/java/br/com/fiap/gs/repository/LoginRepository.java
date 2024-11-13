package br.com.fiap.gs.repository;

import br.com.fiap.gs.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    UserDetails findByEmail(String email);

}
