
package br.com.entidade;

import br.com.controller.SessaoUsuario;
import java.sql.SQLException;

public class LoginEntity {

    public boolean consultarLogin(String usuario, String senha)
            throws SQLException {

        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "SELECT ID_Usuario, Nome, CPF FROM Usuario "
           + "WHERE Email_Institucional = ? AND Senha = ?";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setString(1, usuario);
            dao.pst.setString(2, senha);

            dao.rs = dao.pst.executeQuery();

            if (dao.rs.next()) {
                int idUsuario = dao.rs.getInt("ID_Usuario");
                SessaoUsuario.setIdUsuario(idUsuario);
                SessaoUsuario.setNome(dao.rs.getString("Nome"));
                SessaoUsuario.setCpf(dao.rs.getString("CPF"));

                String sqlFuncionario = "SELECT ID_Funcionario FROM Funcionario WHERE ID_Usuario = ?";
                dao.pst = dao.con.prepareStatement(sqlFuncionario);
                dao.pst.setInt(1, idUsuario);
                dao.rs = dao.pst.executeQuery();
                SessaoUsuario.setFuncionario(dao.rs.next());

                return true;
            }

            return false;

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
