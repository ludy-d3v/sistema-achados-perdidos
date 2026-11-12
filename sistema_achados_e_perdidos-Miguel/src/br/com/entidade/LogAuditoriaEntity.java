package br.com.entidade;

import java.sql.SQLException;

public class LogAuditoriaEntity {

    public void registrarAcao(String acaoRealizada, int idUsuario) throws SQLException {
        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "INSERT INTO Log_Auditoria (Acao_Realizada, Data_Hora_Acao, ID_Usuario) "
                    + "VALUES (?, NOW(), ?)";

            dao.pst = dao.con.prepareStatement(sql);
            dao.pst.setString(1, acaoRealizada);
            dao.pst.setInt(2, idUsuario);
            dao.pst.executeUpdate();

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
}
