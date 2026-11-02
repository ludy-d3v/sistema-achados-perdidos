
package br.com.entidade;

import br.com.controller.ReivindicacaoResumo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReivindicacaoEntity {

    public boolean criarReivindicacao(int idRetirante, int idObjeto) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "INSERT INTO Reivindicacao "
                    + "(Data_Reivindicacao, Status_Reivindicacao, ID_Retirante, ID_Objeto) "
                    + "VALUES (NOW(), 'Pendente', ?, ?)";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setInt(1, idRetirante);
            dao.pst.setInt(2, idObjeto);

            return dao.pst.executeUpdate() > 0;

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
                    "SELECT r.ID_Reivindicacao, r.Data_Reivindicacao, r.Status_Reivindicacao, "
                  + "o.Descricao_Generica, o.Categoria, o.Campus "
                  + "FROM Reivindicacao r "
                  + "INNER JOIN Objeto o ON r.ID_Objeto = o.ID_Objeto "
                  + "WHERE r.ID_Retirante = ?"
            );

            if (nomeObjeto != null && !nomeObjeto.isEmpty()) {
                sql.append(" AND o.Descricao_Generica LIKE ?");
            }

            sql.append(" ORDER BY r.Data_Reivindicacao DESC");

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
}
