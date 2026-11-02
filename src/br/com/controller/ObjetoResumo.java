
package br.com.controller;

import java.time.LocalDate;

public class ObjetoResumo {

    private int id;
    private String descricao;
    private String categoria;
    private String campus;
    private String status;
    private LocalDate dataAchado;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDataAchado() { return dataAchado; }
    public void setDataAchado(LocalDate dataAchado) { this.dataAchado = dataAchado; }
}
