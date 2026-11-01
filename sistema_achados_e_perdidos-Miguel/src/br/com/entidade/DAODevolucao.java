/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

/**
 *
 * @author migue
 */
public class DAODevolucao extends DAO {
    
public class ObjetoEntity {

  
    
public int buscarIdobjeto(String email) throws SQLException {
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

    
}
