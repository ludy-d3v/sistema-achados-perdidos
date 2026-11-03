
package br.com.controller;

import br.com.entidade.ReivindicacaoEntity;
import br.com.entidade.RetiranteEntity;
import java.sql.SQLException;
import java.util.List;

public class ReivindicacaoController {

    private String nomeRetirante;
    private String documentoRetirante;
    private String telefoneRetirante;
    private int idObjeto;

    public void setNomeRetirante(String nomeRetirante) {
        this.nomeRetirante = nomeRetirante;
    }

    public void setDocumentoRetirante(String documentoRetirante) {
        this.documentoRetirante = documentoRetirante;
    }

    public void setTelefoneRetirante(String telefoneRetirante) {
        this.telefoneRetirante = telefoneRetirante;
    }

    public void setIdObjeto(int idObjeto) {
        this.idObjeto = idObjeto;
    }

    public boolean reivindicarObjeto() throws SQLException {
        if (nomeRetirante == null || nomeRetirante.trim().isEmpty()
                || documentoRetirante == null || documentoRetirante.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Não foi possível identificar o usuário logado."
            );
        }

        if (idObjeto <= 0) {
            throw new IllegalArgumentException(
                    "Selecione um objeto para reivindicar."
            );
        }

        RetiranteEntity retiranteEntity = new RetiranteEntity();

        int idRetirante = retiranteEntity.buscarPorDocumento(documentoRetirante.trim());

        if (idRetirante == -1) {
            idRetirante = retiranteEntity.cadastrarRetirante(
                    nomeRetirante.trim(), documentoRetirante.trim(), telefoneRetirante
            );
        }

        if (idRetirante == -1) {
            throw new IllegalArgumentException(
                    "Não foi possível cadastrar o retirante."
            );
        }

        ReivindicacaoEntity reivindicacaoEntity = new ReivindicacaoEntity();

        if (reivindicacaoEntity.jaReivindicou(idRetirante, idObjeto)) {
            throw new IllegalArgumentException(
                    "Você já reivindicou este objeto anteriormente."
            );
        }

        return reivindicacaoEntity.criarReivindicacao(idRetirante, idObjeto);
    }

    public int buscarIdRetirantePorCpf(String cpf) throws SQLException {
        RetiranteEntity retiranteEntity = new RetiranteEntity();
        return retiranteEntity.buscarPorDocumento(cpf);
    }

    public int contarReivindicacoesTotal() throws SQLException {
        ReivindicacaoEntity entity = new ReivindicacaoEntity();
        return entity.contarTotal();
    }

    public int contarReivindicacoes(int idRetirante, String status) throws SQLException {
        ReivindicacaoEntity entity = new ReivindicacaoEntity();
        return entity.contarPorRetirante(idRetirante, status);
    }

    public List<ReivindicacaoResumo> listarMinhasReivindicacoes(int idRetirante, String nomeObjeto) throws SQLException {
        ReivindicacaoEntity entity = new ReivindicacaoEntity();
        return entity.listarPorRetirante(idRetirante, nomeObjeto);
    }
    
    public boolean atualizarStatus(int idReivindicacao, String status) throws SQLException {
        ReivindicacaoEntity entity = new ReivindicacaoEntity();
        return entity.atualizarStatus(idReivindicacao, status);
    } 
    
    public int contarReivindicacoesRelatorio(PeriodoRelatorio periodo, String status) throws SQLException {
        ReivindicacaoEntity entity = new ReivindicacaoEntity();
        return entity.contarRelatorio(periodo, status);
    }
}
