/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controller;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author migue
 */
public class Devolucao {
    private int idobj;
    private String descricao;
    private String detalhesSensiveis;
    private String categoria;
    private LocalDate dataAchado;
    private LocalTime horaAchado;
    private String campus;
    private String bloco;
    private String ambiente;
    private LocalDate dataLimiteGuarda;
    private int idUsuario;
    private String emailUsuario;
    private String nome_obj;
    
    
    
    
    public int getIdbj() {
        return idobj;
    }

    public void setIdobj(int idobj) {
        this.idobj = idobj;
    }

    
    
    public String getNome_obj() {
        return nome_obj;
    }

    public void setNome_obj(String nome_obj) {
        this.nome_obj = nome_obj;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDetalhesSensiveis() {
        return detalhesSensiveis;
    }

    public void setDetalhesSensiveis(String detalhesSensiveis) {
        this.detalhesSensiveis = detalhesSensiveis;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getDataAchado() {
        return dataAchado;
    }

    public void setDataAchado(LocalDate dataAchado) {
        this.dataAchado = dataAchado;
    }

    public LocalTime getHoraAchado() {
        return horaAchado;
    }

    public void setHoraAchado(LocalTime horaAchado) {
        this.horaAchado = horaAchado;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public LocalDate getDataLimiteGuarda() {
        return dataLimiteGuarda;
    }

    public void setDataLimiteGuarda(LocalDate dataLimiteGuarda) {
        this.dataLimiteGuarda = dataLimiteGuarda;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
    
    
    
    
}
