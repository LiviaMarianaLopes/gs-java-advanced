package br.com.fiap.gs.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GS_CADSATRO")
public class Cadastro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "email")
    private String email;
    @Column(name = "rg")
    private Long rg;
    @Column(name = "cpf")
    private Long cpf;
    @Column(name = "senha")
    private String senha;
    @OneToOne
    @JoinColumn(name = "id_login", referencedColumnName = "id")
    private Login login;
}