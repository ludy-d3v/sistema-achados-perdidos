
package br.com.entidade;

import br.com.controller.ReivindicacaoResumo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.controller.Reivindicacao;
import br.com.controller.PeriodoRelatorio;
import java.sql.Timestamp;

public class ReivindicacaoEntity {

    public boolean existePendentePorObjeto(int idObjeto) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "SELECT COUNT(*) AS total FROM Reivindicacao "
                       + "WHERE ID_Objeto = ? AND Status_Reivindicacao = 'Pendente'";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setInt(1, idObjeto);
            dao.rs = dao.pst.executeQuery();

            return dao.rs.next() && dao.rs.getInt("total") > 0;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public String criarReivindicacao(int idRetirante, int idObjeto) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "INSERT INTO Reivindicacao "
                    + "(Data_Reivindicacao, Status_Reivindicacao, ID_Retirante, ID_Objeto) "
                    + "VALUES (NOW(), 'Pendente', ?, ?)";

            dao.pst = dao.con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            dao.pst.setInt(1, idRetirante);
            dao.pst.setInt(2, idObjeto);

            if (dao.pst.executeUpdate() == 0) {
                return null;
            }

            dao.rs = dao.pst.getGeneratedKeys();

            if (!dao.rs.next()) {
                return null;
            }

            int idReivindicacao = dao.rs.getInt(1);

            dao.pst = dao.con.prepareStatement("SELECT fn_gerar_protocolo_reivindicacao(?) AS protocolo");
            dao.pst.setInt(1, idReivindicacao);
            dao.rs = dao.pst.executeQuery();

            return dao.rs.next() ? dao.rs.getString("protocolo") : null;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public boolean jaReivindicou(int idRetirante, int idObjeto) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "SELECT COUNT(*) AS total FROM Reivindicacao "
                       + "WHERE ID_Retirante = ? AND ID_Objeto = ?";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setInt(1, idRetirante);
            dao.pst.setInt(2, idObjeto);
            dao.rs = dao.pst.executeQuery();

            return dao.rs.next() && dao.rs.getInt("total") > 0;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public int contarTotal() throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "SELECT COUNT(*) AS total FROM Reivindicacao";

            dao.pst = dao.con.prepareStatement(sql);
            dao.rs = dao.pst.executeQuery();

            if (dao.rs.next()) {
                return dao.rs.getInt("total");
            }

            return 0;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public int contarPorRetirante(int idRetirante, String status) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = status == null
                    ? "SELECT COUNT(*) AS total FROM Reivindicacao WHERE ID_Retirante = ?"
                    : "SELECT COUNT(*) AS total FROM Reivindicacao WHERE ID_Retirante = ? AND Status_Reivindicacao = ?";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setInt(1, idRetirante);
            if (status != null) {
                dao.pst.setString(2, status);
            }

            dao.rs = dao.pst.executeQuery();

            if (dao.rs.next()) {
                return dao.rs.getInt("total");
            }

            return 0;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public List<ReivindicacaoResumo> listarPorRetirante(int idRetirante, String nomeObjeto) throws SQLException {
        DAO dao = new DAO();
        List<ReivindicacaoResumo> resultado = new ArrayList<>();

        try {
            dao.abrirBanco();

            StringBuilder sql = new StringBuilder(
                    "SELECT ID_Reivindicacao, Data_Reivindicacao, Status_Reivindicacao, "
                  + "Descricao_Generica, Categoria, Campus "
                  + "FROM reivindicacoes_por_retirante "
                  + "WHERE ID_Retirante = ?"
            );

            if (nomeObjeto != null && !nomeObjeto.isEmpty()) {
                sql.append(" AND Descricao_Generica LIKE ?");
            }

            sql.append(" ORDER BY Data_Reivindicacao DESC");

            dao.pst = dao.con.prepareStatement(sql.toString());
            dao.pst.setInt(1, idRetirante);

            if (nomeObjeto != null && !nomeObjeto.isEmpty()) {
                dao.pst.setString(2, "%" + nomeObjeto + "%");
            }

            dao.rs = dao.pst.executeQuery();

            while (dao.rs.next()) {
                ReivindicacaoResumo resumo = new ReivindicacaoResumo();
                resumo.setId(dao.rs.getInt("ID_Reivindicacao"));
                resumo.setDataReivindicacao(dao.rs.getTimestamp("Data_Reivindicacao").toLocalDateTime());
                resumo.setStatus(dao.rs.getString("Status_Reivindicacao"));
                resumo.setDescricaoObjeto(dao.rs.getString("Descricao_Generica"));
                resumo.setCategoriaObjeto(dao.rs.getString("Categoria"));
                resumo.setCampusObjeto(dao.rs.getString("Campus"));
                resultado.add(resumo);
            }

            return resultado;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
    public List<Reivindicacao> consultarReivindicacoes() throws SQLException {

    DAO dao = new DAO();
    List<Reivindicacao> lista = new ArrayList<>();

    try {
        dao.abrirBanco();

        String sql = "SELECT "
                + "ID_Reivindicacao, "
                + "Data_Reivindicacao, "
                + "Status_Reivindicacao, "
                + "Observacao, "
                + "ID_Objeto, "
                + "ID_Retirante, "
                + "Nome_Completo, "
                + "Documento_Identificacao "
                + "FROM consultar_reivindicacoes "
                + "ORDER BY Data_Reivindicacao DESC";

        dao.pst = dao.con.prepareStatement(sql);
        dao.rs = dao.pst.executeQuery();

        while (dao.rs.next()) {

            Reivindicacao reivindicacao = new Reivindicacao();

            reivindicacao.setIdreivindicacao(
                    dao.rs.getInt("ID_Reivindicacao"));

            reivindicacao.setData_reivindicacao(
                    dao.rs.getTimestamp("Data_Reivindicacao").toLocalDateTime());

            reivindicacao.setStatus_reivindicacao(
                    dao.rs.getString("Status_Reivindicacao"));

            reivindicacao.setObservacao(
                    dao.rs.getString("Observacao"));

            reivindicacao.setId_objeto(
                    dao.rs.getInt("ID_Objeto"));

            reivindicacao.setId_retirante(
                    dao.rs.getInt("ID_Retirante"));

            reivindicacao.setNome_aluno(
                    dao.rs.getString("Nome_Completo"));

            reivindicacao.setDocumento_identificacao(
                    dao.rs.getString("Documento_Identificacao"));

            lista.add(reivindicacao);
        }

        return lista;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
   
    public boolean atualizarStatus(int idReivindicacao, String status) throws SQLException {
    if (!status.equals("Aprovada") && !status.equals("Recusada")) {
        throw new IllegalArgumentException("Status inválido.");
    }

    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "UPDATE Reivindicacao "
                   + "SET Status_Reivindicacao = ? "
                   + "WHERE ID_Reivindicacao = ? "
                   + "AND Status_Reivindicacao = 'Pendente'";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setString(1, status);
        dao.pst.setInt(2, idReivindicacao);

        return dao.pst.executeUpdate() > 0;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
public int contarRelatorio(PeriodoRelatorio periodo, String status) throws SQLException {

    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) AS total FROM Reivindicacao WHERE 1=1"
        );

        if (periodo != null && !periodo.isTodoPeriodo()) {
            sql.append(" AND Data_Reivindicacao >= ?");
            sql.append(" AND Data_Reivindicacao < ?");
        }

        if (status != null) {
            sql.append(" AND Status_Reivindicacao = ?");
        }

        dao.pst = dao.con.prepareStatement(sql.toString());

        int parametro = 1;

        if (periodo != null && !periodo.isTodoPeriodo()) {

            if (periodo.getDataInicial() == null || periodo.getDataFinal() == null) {
                throw new IllegalArgumentException("Período personalizado incompleto.");
            }

            dao.pst.setTimestamp(
                    parametro++,
                    Timestamp.valueOf(periodo.getDataInicial().atStartOfDay())
            );

            dao.pst.setTimestamp(
                    parametro++,
                    Timestamp.valueOf(periodo.getDataFinal().plusDays(1).atStartOfDay())
            );
        }

        if (status != null) {
            dao.pst.setString(parametro, status);
        }

        dao.rs = dao.pst.executeQuery();

        if (dao.rs.next()) {
            return dao.rs.getInt("total");
        }

        return 0;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
}
