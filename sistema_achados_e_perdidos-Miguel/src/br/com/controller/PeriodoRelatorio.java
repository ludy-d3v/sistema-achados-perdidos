package br.com.controller;

import java.time.LocalDate;

public class PeriodoRelatorio {

    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private boolean todoPeriodo;

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public boolean isTodoPeriodo() {
        return todoPeriodo;
    }

    public void setTodoPeriodo(boolean todoPeriodo) {
        this.todoPeriodo = todoPeriodo;
    }
}