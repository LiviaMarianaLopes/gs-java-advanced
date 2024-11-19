package br.com.fiap.odontoprev.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "OP_PACIENTE")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_paciente")
    private String nome;

    @Column(name = "data_de_nascimento")
    private Date dataNascimento;

    @Column(name = "email_paciente")
    private String email;
    @Column(name = "CPF_PACIENTE")
    private Long cpf;
    @Column(name = "telefone_paciente")
    private Long telefone;
    @ManyToOne
    @JoinColumn(name = "id_genero", referencedColumnName = "id")
    private Genero genero;

    @ManyToOne
    @JoinColumn(name = "id_endereco", referencedColumnName = "id")
    private Endereco endereco;

    @OneToOne
    @JoinColumn(name = "id_login", referencedColumnName = "id")
    private Login login;
    @Column(name = "CLIENTE_SUSPEITO")
    private String suspeito;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Long getTelefone() {
        return telefone;
    }

    public void setTelefone(Long telefone) {
        this.telefone = telefone;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getSuspeito() {
        return suspeito;
    }

    public void setSuspeito(String suspeito) {
        this.suspeito = suspeito;
    }
}
