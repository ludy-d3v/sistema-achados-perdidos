
package br.com.controller;

import br.com.entidade.ObjetoEntity;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ObjetoController {

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

public boolean cadastrarObjeto() throws SQLException {
    if (descricao == null || descricao.trim().isEmpty()
            || categoria == null || categoria.trim().isEmpty()
            || dataAchado == null || horaAchado == null
            || campus == null || campus.trim().isEmpty()
            || ambiente == null || ambiente.trim().isEmpty()
            || dataLimiteGuarda == null) {

        throw new IllegalArgumentException(
                "Preencha todos os campos obrigatórios."
        );
    }

    if (emailUsuario == null || emailUsuario.trim().isEmpty()) {
        throw new IllegalArgumentException(
                "Informe o e-mail institucional do usuário."
        );
    }

    ObjetoEntity entity = new ObjetoEntity();

    int idEncontrado = entity.buscarIdUsuarioPorEmail(emailUsuario);

    if (idEncontrado == -1) {
        throw new IllegalArgumentException(
                "Não existe usuário cadastrado com esse e-mail."
        );
    }

    return entity.cadastrarObjeto(
            descricao.trim(),
            detalhesSensiveis,
            categoria.trim(),
            dataAchado,
            horaAchado,
            campus.trim(),
            bloco,
            ambiente.trim(),
            dataLimiteGuarda,
            idEncontrado
    );
}

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getDetalhesSensiveis() { return detalhesSensiveis; }
    public void setDetalhesSensiveis(String detalhesSensiveis) {
        this.detalhesSensiveis = detalhesSensiveis;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getDataAchado() { return dataAchado; }
    public void setDataAchado(LocalDate dataAchado) { this.dataAchado = dataAchado; }

    public LocalTime getHoraAchado() { return horaAchado; }
    public void setHoraAchado(LocalTime horaAchado) { this.horaAchado = horaAchado; }

    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }

    public String getBloco() { return bloco; }
    public void setBloco(String bloco) { this.bloco = bloco; }

    public String getAmbiente() { return ambiente; }
    public void setAmbiente(String ambiente) { this.ambiente = ambiente; }

    public LocalDate getDataLimiteGuarda() { return dataLimiteGuarda; }
    public void setDataLimiteGuarda(LocalDate dataLimiteGuarda) {
        this.dataLimiteGuarda = dataLimiteGuarda;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    
    public String getEmailUsuario() {
    return emailUsuario;
}

    public void setEmailUsuario(String emailUsuario) {
       this.emailUsuario = emailUsuario;
    }

}
