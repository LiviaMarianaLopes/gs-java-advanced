package br.com.fiap.gs.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GS_CADASTRO")
public class Cadastro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "email")
    private String email;
    @Column(name = "numero_rg")
    private Long rg;
    @Column(name = "numero_cpf")
    private Long cpf;
    @Column(name = "senha")
    private String senha;
    @OneToOne
    @JoinColumn(name = "id_login", referencedColumnName = "id")
    private Login login;

    public Cadastro(String nome, String email, Long rg, Long cpf, String senha, Login login) {
        this.nome = nome;
        this.email = email;
        this.rg = rg;
        this.cpf = cpf;
        this.senha = senha;
        this.login = login;
    }
}