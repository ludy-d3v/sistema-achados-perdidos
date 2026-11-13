package br.com.entidade;

import br.com.controller.Devolucao;
import br.com.controller.Reivindicacao;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class DAODevolucoes extends DAO{


    public int buscarIdUsuario(Devolucao d) throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT * FROM Objeto WHERE ID_Objeto = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setInt(1, d.getIdbj());
        dao.rs = dao.pst.executeQuery();

        if (dao.rs.next()) {
            d.setIdUsuario(dao.rs.getInt("ID_Usuario"));
            d.setIdobj(dao.rs.getInt("ID_Objeto"));
            d.setAmbiente(dao.rs.getString("Ambiente"));
            d.setBloco(dao.rs.getString("Bloco"));
            d.setCampus(dao.rs.getString("Campus"));
            d.setCategoria(dao.rs.getString("Categoria"));
            d.setDataAchado(dao.rs.getDate("Data_Achado").toLocalDate());
            d.setDataLimiteGuarda(dao.rs.getDate("Data_Limite_Guarda").toLocalDate());
            d.setDescricao(dao.rs.getString("Descricao_Generica"));
            d.setDetalhesSensiveis(dao.rs.getString("Detalhes_Sensiveis"));
            d.setHoraAchado(dao.rs.getTime("Hora_Achado").toLocalTime());
            // Objeto não tem coluna de nome própria, reaproveita a descrição genérica
            d.setNome_obj(dao.rs.getString("Descricao_Generica"));

            return 1;
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum resultado encontrado!");
            return 0;
        }

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
    }
    
    
   public int buscarIdReivindicacao(Reivindicacao r, Devolucao d) throws SQLException {

    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT * "
                   + "FROM consultar_reivindicacoes "
                   + "WHERE ID_Reivindicacao = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setInt(1, r.getIdreivindicacao());
        dao.rs = dao.pst.executeQuery();

        if (dao.rs.next()) {

            // Dados da tabela Reivindicacao
            r.setId_objeto(dao.rs.getInt("ID_Objeto"));
            r.setId_retirante(dao.rs.getInt("ID_Retirante"));
            r.setIdreivindicacao(dao.rs.getInt("ID_Reivindicacao"));
            r.setObservacao(dao.rs.getString("Observacao"));
            r.setStatus_reivindicacao(dao.rs.getString("Status_Reivindicacao"));

            // Dados do Retirante
            r.setNome_aluno(dao.rs.getString("Nome_Completo"));
            r.setDocumento_identificacao(dao.rs.getString("Documento_Identificacao"));

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

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
   
     public String cadastrarDevolucao(int idObjeto, int idRetirante, int idUsuario, int idReivindicacao, LocalDate dataDevolucao, String assinaturaDigital) throws SQLException {

    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "INSERT INTO Devolucao "
                + "(ID_Objeto, ID_Retirante, ID_Usuario, ID_Reivindicacao, "
                + "Data_Devolucao, Assinatura_Digital) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        dao.pst = dao.con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

        dao.pst.setInt(1, idObjeto);
        dao.pst.setInt(2, idRetirante);
        dao.pst.setInt(3, idUsuario);
        dao.pst.setInt(4, idReivindicacao);
        dao.pst.setDate(5, Date.valueOf(dataDevolucao));
        dao.pst.setString(6, assinaturaDigital);

        if (dao.pst.executeUpdate() == 0) {
            return null;
        }

        dao.rs = dao.pst.getGeneratedKeys();

        if (!dao.rs.next()) {
            return null;
        }

        int idDevolucao = dao.rs.getInt(1);

        dao.pst = dao.con.prepareStatement("SELECT fn_gerar_protocolo_devolucao(?) AS protocolo");
        dao.pst.setInt(1, idDevolucao);
        dao.rs = dao.pst.executeQuery();

        String protocolo = dao.rs.next() ? dao.rs.getString("protocolo") : null;

        if (protocolo != null) {
            new LogAuditoriaEntity().registrarAcao(
                    "Devolução registrada - Protocolo " + protocolo + " (Objeto #" + idObjeto + ")",
                    idUsuario
            );
        }

        return protocolo;

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

public int contarDevolucoesPorRetirante(int idRetirante) throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT COUNT(*) AS total FROM Devolucao WHERE ID_Retirante = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setInt(1, idRetirante);
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

}