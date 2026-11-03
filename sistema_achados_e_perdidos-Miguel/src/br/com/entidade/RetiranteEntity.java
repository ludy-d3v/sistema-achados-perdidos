
package br.com.entidade;

import java.sql.SQLException;
import java.sql.Statement;

public class RetiranteEntity {

    public int buscarPorDocumento(String documento) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "SELECT ID_Retirante FROM Retirante WHERE Documento_Identificacao = ?";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setString(1, documento);

            dao.rs = dao.pst.executeQuery();

            if (dao.rs.next()) {
                return dao.rs.getInt("ID_Retirante");
            }

            return -1;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public int cadastrarRetirante(String nome, String documento, String telefone) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "INSERT INTO Retirante (Nome_Completo, Documento_Identificacao, Telefone) "
                       + "VALUES (?, ?, ?)";

            dao.pst = dao.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            dao.pst.setString(1, nome);
            dao.pst.setString(2, documento);
            dao.pst.setString(3, telefone);

            dao.pst.executeUpdate();

            dao.rs = dao.pst.getGeneratedKeys();

            if (dao.rs.next()) {
                return dao.rs.getInt(1);
            }

            return -1;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
}
