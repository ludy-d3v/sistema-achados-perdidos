
package br.com.controller;

import java.time.LocalDateTime;

public class ReivindicacaoResumo {

    private int id;
    private LocalDateTime dataReivindicacao;
    private String status;
    private String descricaoObjeto;
    private String categoriaObjeto;
    private String campusObjeto;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDataReivindicacao() { return dataReivindicacao; }
    public void setDataReivindicacao(LocalDateTime dataReivindicacao) {
        this.dataReivindicacao = dataReivindicacao;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescricaoObjeto() { return descricaoObjeto; }
    public void setDescricaoObjeto(String descricaoObjeto) { this.descricaoObjeto = descricaoObjeto; }

    public String getCategoriaObjeto() { return categoriaObjeto; }
    public void setCategoriaObjeto(String categoriaObjeto) { this.categoriaObjeto = categoriaObjeto; }

    public String getCampusObjeto() { return campusObjeto; }
    public void setCampusObjeto(String campusObjeto) { this.campusObjeto = campusObjeto; }
}
