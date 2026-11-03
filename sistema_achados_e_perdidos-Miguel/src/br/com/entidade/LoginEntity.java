
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
    
        public boolean consultarCadastro(String usuario, String CPF)
            throws SQLException {

        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "SELECT ID_Usuario FROM Usuario "
           + "WHERE Email_Institucional = ? AND CPF = ?";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setString(1, usuario);
            dao.pst.setString(2, CPF);

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

   
public boolean cadastrarUsuario( String nome, String usuario, String senha,String CPF) throws SQLException {

    DAO dao = new DAO();

    try {
       
        dao.abrirBanco();
        dao.con.setAutoCommit(false);
        String sql ="INSERT INTO Usuario "
                + "(ID_Usuario, Nome, Email_Institucional, Senha, CPF) "
                + "VALUES (NULL,?, ?, ?, ?)";

        dao.pst = dao.con.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);

        dao.pst.setString(1, nome);
        dao.pst.setString(2, usuario);
        dao.pst.setString(3, senha);
        dao.pst.setString(4, CPF);

        int resultado = dao.pst.executeUpdate();

        if (resultado == 0) {
            dao.con.rollback();
            return false;
        }

        dao.rs = dao.pst.getGeneratedKeys();

        if (!dao.rs.next()) {
            dao.con.rollback();
            return false;
        }

        int idUsuario = dao.rs.getInt(1);

        if (usuario.toLowerCase().endsWith("@p.ucb.br")) {

            String sqlFuncionario ="INSERT INTO Funcionario (ID_Usuario) "
                                     + "VALUES (?)";

            dao.pst = dao.con.prepareStatement(sqlFuncionario);
            dao.pst.setInt(1, idUsuario);
            dao.pst.executeUpdate();
        }

        dao.con.commit();

        return true;

    } catch (SQLException e) {

        if (dao.con != null) {
            dao.con.rollback();
        }throw e;

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
