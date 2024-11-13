package br.com.fiap.gs.model;

import jakarta.persistence.*;

@Entity
@Table(name = "OP_BAIRRO")
public class Bairro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME_BAIRRO")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "id")
    private Cidade cidade;

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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}