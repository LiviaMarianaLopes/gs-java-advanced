package br.com.fiap.gs.model;

import jakarta.persistence.*;

@Entity
@Table(name = "OP_CIDADE")
public class Cidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME_CIDADE")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "id")
    private Estado estado;

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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
