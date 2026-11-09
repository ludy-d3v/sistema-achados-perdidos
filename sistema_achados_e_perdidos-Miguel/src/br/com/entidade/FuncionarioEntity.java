
package br.com.entidade;

import br.com.controller.UsuarioResumo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioEntity {

    public List<UsuarioResumo> listarUsuarios(String filtro) throws SQLException {
        DAO dao = new DAO();
        List<UsuarioResumo> resultado = new ArrayList<>();

        try {
            dao.abrirBanco();

            StringBuilder sql = new StringBuilder(
                    "SELECT u.ID_Usuario, u.Nome, u.CPF, u.Email_Institucional, "
                  + "f.Cargo, f.Setor "
                  + "FROM Usuario u "
                  + "LEFT JOIN Funcionario f ON f.ID_Usuario = u.ID_Usuario"
            );

            if (filtro != null && !filtro.isEmpty()) {
                sql.append(" WHERE u.Nome LIKE ? OR u.Email_Institucional LIKE ? OR u.CPF LIKE ?");
            }

            sql.append(" ORDER BY u.Nome");

            dao.pst = dao.con.prepareStatement(sql.toString());

            if (filtro != null && !filtro.isEmpty()) {
                String comodim = "%" + filtro + "%";
                dao.pst.setString(1, comodim);
                dao.pst.setString(2, comodim);
                dao.pst.setString(3, comodim);
            }

            dao.rs = dao.pst.executeQuery();

            while (dao.rs.next()) {
                UsuarioResumo usuario = new UsuarioResumo();
                usuario.setId(dao.rs.getInt("ID_Usuario"));
                usuario.setNome(dao.rs.getString("Nome"));
                usuario.setCpf(dao.rs.getString("CPF"));
                usuario.setEmail(dao.rs.getString("Email_Institucional"));
                usuario.setCargo(dao.rs.getString("Cargo"));
                usuario.setSetor(dao.rs.getString("Setor"));
                usuario.setFuncionario(dao.rs.getString("Cargo") != null);
                resultado.add(usuario);
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

    public boolean promover(int idUsuario, String cargo, String setor) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "INSERT INTO Funcionario (Cargo, Setor, ID_Usuario) VALUES (?, ?, ?)";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setString(1, cargo);
            dao.pst.setString(2, setor);
            dao.pst.setInt(3, idUsuario);

            return dao.pst.executeUpdate() > 0;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public boolean atualizarCargoSetor(int idUsuario, String cargo, String setor) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "UPDATE Funcionario SET Cargo = ?, Setor = ? WHERE ID_Usuario = ?";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setString(1, cargo);
            dao.pst.setString(2, setor);
            dao.pst.setInt(3, idUsuario);

            return dao.pst.executeUpdate() > 0;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
    
    public boolean deletarUsuario(int idUsuario) throws SQLException {

    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "DELETE FROM Usuario "
                + "WHERE ID_Usuario = ? "
                + "AND NOT EXISTS ("
                + "SELECT 1 FROM Funcionario f "
                + "WHERE f.ID_Usuario = Usuario.ID_Usuario"
                + ") "
                + "AND NOT EXISTS ("
                + "SELECT 1 FROM Administrador a "
                + "WHERE a.ID_Usuario = Usuario.ID_Usuario"
                + ")";

        dao.pst = dao.con.prepareStatement(sql);

        dao.pst.setInt(1, idUsuario);

        return dao.pst.executeUpdate() > 0;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println(
                    "Erro ao fechar recursos: " + e.getMessage()
            );
        }
    }
}

    public boolean removerFuncionario(int idUsuario) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "DELETE FROM Funcionario WHERE ID_Usuario = ?";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setInt(1, idUsuario);

            return dao.pst.executeUpdate() > 0;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
}
