
package br.com.entidade;

import java.sql.SQLException;

public class LoginEntity {

    public boolean consultarLogin(String usuario, String senha)
            throws SQLException {

        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "SELECT ID_Usuario FROM Usuario "
           + "WHERE Email_Institucional = ? AND Senha = ?";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setString(1, usuario);
            dao.pst.setString(2, senha);

            dao.rs = dao.pst.executeQuery();

            return dao.rs.next();

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println(
                    "Erro ao fechar conexão: " + e.getMessage()
                );
            }
        }
    }
}
