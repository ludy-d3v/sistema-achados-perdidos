
package br.com.controller;

import br.com.entidade.DAODevolucoes;
import java.sql.SQLException;

public class DevolucaoController {

    public int contarDevolucoesPorRetirante(int idRetirante) throws SQLException {
        DAODevolucoes entity = new DAODevolucoes();
        return entity.contarDevolucoesPorRetirante(idRetirante);
    }
}
