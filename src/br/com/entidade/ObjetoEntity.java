
package br.com.entidade;

import br.com.controller.ObjetoResumo;
import br.com.controller.PeriodoRelatorio;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

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

public List<ObjetoResumo> buscarObjetos(
        String nome,
        String categoria,
        LocalDate depoisDe,
        LocalDate antesDe) throws SQLException {

    DAO dao = new DAO();
    List<ObjetoResumo> resultado = new ArrayList<>();

    try {
        dao.abrirBanco();

        StringBuilder sql = new StringBuilder(
                "SELECT ID_Objeto, Descricao_Generica, Categoria, Campus, Status_Atual, Data_Achado "
              + "FROM Objeto WHERE 1=1");

        if (nome != null && !nome.isEmpty()) {
            sql.append(" AND Descricao_Generica LIKE ?");
        }
        if (categoria != null && !categoria.isEmpty()) {
            sql.append(" AND Categoria = ?");
        }
        if (depoisDe != null) {
            sql.append(" AND Data_Achado >= ?");
        }
        if (antesDe != null) {
            sql.append(" AND Data_Achado <= ?");
        }
        sql.append(" ORDER BY Data_Achado DESC");

        dao.pst = dao.con.prepareStatement(sql.toString());

        int indice = 1;
        if (nome != null && !nome.isEmpty()) {
            dao.pst.setString(indice++, "%" + nome + "%");
        }
        if (categoria != null && !categoria.isEmpty()) {
            dao.pst.setString(indice++, categoria);
        }
        if (depoisDe != null) {
            dao.pst.setDate(indice++, Date.valueOf(depoisDe));
        }
        if (antesDe != null) {
            dao.pst.setDate(indice++, Date.valueOf(antesDe));
        }

        dao.rs = dao.pst.executeQuery();

        while (dao.rs.next()) {
            resultado.add(mapearObjeto(dao.rs));
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

public boolean atualizarObjeto(
        int id,
        String descricao,
        String categoria,
        String campus) throws SQLException {

    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "UPDATE Objeto SET "
                + "Descricao_Generica = ?, Categoria = ?, Campus = ? "
                + "WHERE ID_Objeto = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setString(1, descricao);
        dao.pst.setString(2, categoria);
        dao.pst.setString(3, campus);
        dao.pst.setInt(4, id);

        return dao.pst.executeUpdate() > 0;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}

public boolean excluirObjeto(int id) throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "DELETE FROM Objeto WHERE ID_Objeto = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setInt(1, id);

        return dao.pst.executeUpdate() > 0;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}

public String buscarStatusAtual(int id) throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT Status_Atual FROM Objeto WHERE ID_Objeto = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setInt(1, id);
        dao.rs = dao.pst.executeQuery();

        if (dao.rs.next()) {
            return dao.rs.getString("Status_Atual");
        }

        return null;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}

public boolean atualizarStatus(int id, String status) throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "UPDATE Objeto SET Status_Atual = ? WHERE ID_Objeto = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setString(1, status);
        dao.pst.setInt(2, id);

        return dao.pst.executeUpdate() > 0;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}

public int contarTotal() throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT COUNT(*) AS total FROM Objeto";

        dao.pst = dao.con.prepareStatement(sql);
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

public int contarPorStatus(String status) throws SQLException {
    DAO dao = new DAO();

    try {
        dao.abrirBanco();

        String sql = "SELECT COUNT(*) AS total FROM Objeto WHERE Status_Atual = ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setString(1, status);
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

private ObjetoResumo mapearObjeto(java.sql.ResultSet rs) throws SQLException {
    ObjetoResumo objeto = new ObjetoResumo();
    objeto.setId(rs.getInt("ID_Objeto"));
    objeto.setDescricao(rs.getString("Descricao_Generica"));
    objeto.setCategoria(rs.getString("Categoria"));
    objeto.setCampus(rs.getString("Campus"));
    objeto.setStatus(rs.getString("Status_Atual"));
    objeto.setDataAchado(rs.getDate("Data_Achado").toLocalDate());
    return objeto;
}

public List<ObjetoResumo> listarRecentes(int limite) throws SQLException {
    DAO dao = new DAO();
    List<ObjetoResumo> resultado = new ArrayList<>();

    try {
        dao.abrirBanco();

        String sql = "SELECT ID_Objeto, Descricao_Generica, Categoria, Campus, Status_Atual, Data_Achado "
                   + "FROM Objeto "
                   + "ORDER BY Data_Achado DESC "
                   + "LIMIT ?";

        dao.pst = dao.con.prepareStatement(sql);
        dao.pst.setInt(1, limite);

        dao.rs = dao.pst.executeQuery();

        while (dao.rs.next()) {
            resultado.add(mapearObjeto(dao.rs));
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

public List<String> listarCategorias() throws SQLException {
    DAO dao = new DAO();
    List<String> categorias = new ArrayList<>();

    try {
        dao.abrirBanco();

        String sql = "SELECT DISTINCT Categoria FROM Objeto ORDER BY Categoria";

        dao.pst = dao.con.prepareStatement(sql);
        dao.rs = dao.pst.executeQuery();

        while (dao.rs.next()) {
            categorias.add(dao.rs.getString("Categoria"));
        }

        return categorias;

    } finally {
        try {
            dao.fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
public Map<String, Object> gerarRelatorioObjetos(
        PeriodoRelatorio periodo) throws SQLException {

    DAO dao = new DAO();

    Map<String, Object> resultado = new LinkedHashMap<>();

    Map<String, Integer> categorias = new LinkedHashMap<>();
    Map<String, Integer> campus = new LinkedHashMap<>();
    Map<String, Integer> meses = new LinkedHashMap<>();

    try {
        dao.abrirBanco();

        CallableStatement cs = dao.con.prepareCall(
                "{CALL sp_relatorio_objetos(?, ?)}"
        );

        if (periodo == null || periodo.isTodoPeriodo()) {
            cs.setNull(1, java.sql.Types.DATE);
            cs.setNull(2, java.sql.Types.DATE);
        } else {
            if (periodo.getDataInicial() == null
                    || periodo.getDataFinal() == null) {
                throw new IllegalArgumentException(
                        "Período personalizado incompleto."
                );
            }

            cs.setDate(1, Date.valueOf(periodo.getDataInicial()));
            cs.setDate(2, Date.valueOf(periodo.getDataFinal()));
        }

        boolean temResultado = cs.execute();

        int total = 0;
        int conjunto = 0;

        while (true) {

            if (temResultado) {
                try (ResultSet rs = cs.getResultSet()) {

                    if (rs != null) {
                        while (rs.next()) {

                            if (conjunto == 0) {
                                total = rs.getInt("total");

                            } else if (conjunto == 1) {
                                categorias.put(
                                        rs.getString("Categoria"),
                                        rs.getInt("total")
                                );

                            } else if (conjunto == 2) {
                                campus.put(
                                        rs.getString("Campus"),
                                        rs.getInt("total")
                                );

                            } else if (conjunto == 3) {
                                meses.put(
                                        rs.getString("mes"),
                                        rs.getInt("total")
                                );
                            }
                        }
                    }
                }

                conjunto++;

            } else if (cs.getUpdateCount() == -1) {
                break;
            }

            temResultado = cs.getMoreResults();
        }

        resultado.put("total", total);
        resultado.put("categorias", categorias);
        resultado.put("campus", campus);
        resultado.put("meses", meses);

        cs.close();

        return resultado;

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