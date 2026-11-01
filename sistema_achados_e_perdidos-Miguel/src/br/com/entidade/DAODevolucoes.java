package br.com.entidade;

import br.com.controller.Devolucao;
import br.com.controller.Reivindicacao;
import java.awt.HeadlessException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DAODevolucoes extends DAO{
    
    
    public int buscarIdUsuario(Devolucao d) throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT * FROM objeto "
                   + "WHERE ID_Objeto = ?";

        dao.pst= (PreparedStatement) dao.con.prepareStatement(sql);
        dao.pst.setInt(1, d.getIdbj());
        ResultSet tr = dao.pst.executeQuery();
          
            
            if(tr.next()){
                d.setIdUsuario(tr.getInt("id_usuario"));
                d.setIdobj(tr.getInt("ID_Objeto"));
                d.setAmbiente(tr.getString("ambiente"));
                d.setBloco(tr.getString("bloco"));
                d.setCampus(tr.getString("campus"));
                d.setCategoria(tr.getString("categoria"));
                d.setDataAchado(tr.getDate("data_achado").toLocalDate());
                d.setDataLimiteGuarda(tr.getDate("data_limite").toLocalDate());
                d.setDescricao(tr.getString("descricao"));
                d.setDetalhesSensiveis(tr.getString("detalhes"));
                //d.setEmailUsuario(tr.getString("email_usuario"));
                d.setHoraAchado(tr.getTime("hora_achado").toLocalTime());
                d.setNome_obj(tr.getString("nome_obj"));
                
                
            }else{
            JOptionPane.showMessageDialog(null,"Nenhum resultado encontrado!");
            }
        
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro!!" + e.getMessage());
                  
        }
        return 0;
       
    }
    
    
   public int buscarIdReivindicacao(Reivindicacao r, Devolucao d) throws SQLException, Exception {

    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT r.*, "
                   + "rt.ID_Usuario, "
                   + "u.Nome_Completo, "
                   + "u.Matricula "
                   + "FROM reivindicacao r "// inner join para achar o ID do usuario na tabela retirante
                   + "INNER JOIN retirante rt "
                   + "ON r.ID_Retirante = rt.ID_Retirante "
                   + "INNER JOIN usuario u " // inner join para buscar o nome e a matricula na tabela usuario
                   + "ON rt.ID_Usuario = u.ID_Usuario "
                   + "WHERE r.ID_Reivindicacao = ?";

        
        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setInt(1, r.getIdreivindicacao());
        ResultSet tr = dao.pst.executeQuery();

        
        if (tr.next()) {

           
            // Dados da tabela reivindicação
            r.setId_objeto(tr.getInt("ID_Objeto"));
            r.setId_retirante(tr.getInt("ID_Retirante"));
            r.setIdreivindicacao(tr.getInt("ID_Reivindicacao"));
            r.setObservacao(tr.getString("observacao"));
            r.setStatus_reivindicacao(tr.getString("status"));

            
            //inner join para buscar na tabela usuario
            d.setIdUsuario(tr.getInt("ID_Usuario"));

            //dados da tabela usuario
            r.setNome_aluno(sql);
            tr.getString("Nome_Completo");
            r.setMatricula(tr.getString("Matricula"));

            // Busca os dados do objeto
            d.setIdobj(r.getId_objeto());
            buscarIdUsuario(d);

            return 1;

        } else {

            JOptionPane.showMessageDialog(
                null,
                "Nenhuma reivindicação encontrada!"
            );

            return 0;
        }

    } catch (HeadlessException | SQLException e) {

        System.out.println("Erro!! " + e.getMessage());

        return 0;

    } finally {

        dao.fecharBanco();
    }
}
   
     public boolean cadastrarDevolucao(int idObjeto,int idRetirante,int idUsuario,LocalDate dataDevolucao,String assinaturaDigital) throws SQLException {

    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "INSERT INTO Devolucao "
                + "(ID_Objeto, ID_Retirante, ID_Usuario, "
                + "Data_Devolucao, Assinatura_Digital) "
                + "VALUES (?, ?, ?, ?, ?)";

        dao.pst = dao.con.prepareStatement(sql);

        dao.pst.setInt(1, idObjeto);
        dao.pst.setInt(2, idRetirante);
        dao.pst.setInt(3, idUsuario);
        dao.pst.setDate(4, Date.valueOf(dataDevolucao));
        dao.pst.setString(5, assinaturaDigital);

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

    public boolean cadastrarDevolucao(int idbj, int id_retirante, JTextField jTid_funcionario, LocalDate dataDevolucao, String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}