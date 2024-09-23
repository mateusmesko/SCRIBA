package org.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cartorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String observacao;
    private String situacaoId; // ID da situação
    private Long atribuicaoId;

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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }


    public Long getAtribuicaoId() {
        return atribuicaoId;
    }

    public void setAtribuicaoId(Long atribuicaoId) {
        this.atribuicaoId = atribuicaoId;
    }

    public String getSituacaoId() {
        return situacaoId;
    }

    public void setSituacaoId(String situacaoId) {
        this.situacaoId = situacaoId;
    }
}
