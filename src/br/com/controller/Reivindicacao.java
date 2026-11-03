/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controller;

import java.time.LocalDateTime;

/**
 *
 * @author migue
 */
public class Reivindicacao {
    
   private int idreivindicacao;
   private LocalDateTime  data_reivindicacao;
   private String status_reivindicacao;
   private String observacao;
   private int id_objeto;
   private int id_retirante;
   
   
   
    public int getIdreivindicacao() {
        return idreivindicacao;
    }

    public void setIdreivindicacao(int idreivindicacao) {
        this.idreivindicacao = idreivindicacao;
    }

    public LocalDateTime  getData_reivindicacao() {
        return data_reivindicacao;
    }

    public void setData_reivindicacao(LocalDateTime  data_reivindicacao) {
        this.data_reivindicacao = data_reivindicacao;
    }

    public String getStatus_reivindicacao() {
        return status_reivindicacao;
    }

    public void setStatus_reivindicacao(String status_reivindicacao) {
        this.status_reivindicacao = status_reivindicacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getId_retirante() {
        return id_retirante;
    }

    public void setId_retirante(int id_retirante) {
        this.id_retirante = id_retirante;
    }

    public int getId_objeto() {
        return id_objeto;
    }

    public void setId_objeto(int id_objeto) {
        this.id_objeto = id_objeto;
    }
    
     private String nome_aluno;
   private String documento_identificacao; 

    public String getNome_aluno() {
        return nome_aluno;
    }

    public void setNome_aluno(String nome_aluno) {
        this.nome_aluno = nome_aluno;
    }

    public String getDocumento_identificacao() {
        return documento_identificacao;
    }

    public void setDocumento_identificacao(String documento_identificacao) {
        this.documento_identificacao = documento_identificacao;
    }
    
    
    
}
