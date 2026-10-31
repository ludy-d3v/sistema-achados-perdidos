
package br.com.entidade;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ObjetoEntity {

    public boolean cadastrarObjeto(
            String descricao,
            String detalhesSensiveis,
            String categoria,
            LocalDate dataAchado,
            LocalTime horaAchado,
            String campus,
            String bloco,
            String ambiente,
            LocalDate dataLimiteGuarda,
            int idUsuario) throws SQLException {

        DAO dao = new DAO();

        try {
            dao.abrirBanco();

            String sql = "INSERT INTO Objeto "
                    + "(Descricao_Generica, Detalhes_Sensiveis, Categoria, "
                    + "Data_Achado, Hora_Achado, Campus, Bloco, Ambiente, "
                    + "Data_Limite_Guarda, Data_Cadastro, Hora_Cadastro, ID_Usuario) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), CURTIME(), ?)";

            dao.pst = dao.con.prepareStatement(sql);

            dao.pst.setString(1, descricao);
            dao.pst.setString(2, detalhesSensiveis);
            dao.pst.setString(3, categoria);
            dao.pst.setDate(4, Date.valueOf(dataAchado));
            dao.pst.setTime(5, Time.valueOf(horaAchado));
            dao.pst.setString(6, campus);
            dao.pst.setString(7, bloco);
            dao.pst.setString(8, ambiente);
            dao.pst.setDate(9, Date.valueOf(dataLimiteGuarda));
            dao.pst.setInt(10, idUsuario);

            return dao.pst.executeUpdate() > 0;

        } finally {
            try {
                dao.fecharBanco();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
    
public int buscarIdUsuarioPorEmail(String email) throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT ID_Usuario FROM Usuario "
                   + "WHERE Email_Institucional = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setString(1, email.trim());

        dao.rs = dao.pst.executeQuery();

        if (dao.rs.next()) {
            return dao.rs.getInt("ID_Usuario");
        }

        return -1;

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

}
