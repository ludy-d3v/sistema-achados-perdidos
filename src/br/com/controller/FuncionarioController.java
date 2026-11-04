
package br.com.controller;

import br.com.entidade.FuncionarioEntity;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioController {

    public List<UsuarioResumo> listarUsuarios(String filtro) throws SQLException {
        FuncionarioEntity entity = new FuncionarioEntity();
        return entity.listarUsuarios(filtro);
    }

    public boolean promover(int idUsuario, String cargo, String setor) throws SQLException {
        validarCargoSetor(cargo, setor);
        FuncionarioEntity entity = new FuncionarioEntity();
        return entity.promover(idUsuario, cargo.trim(), setor.trim());
    }

    public boolean atualizarCargoSetor(int idUsuario, String cargo, String setor) throws SQLException {
        validarCargoSetor(cargo, setor);
        FuncionarioEntity entity = new FuncionarioEntity();
        return entity.atualizarCargoSetor(idUsuario, cargo.trim(), setor.trim());
    }

    public boolean removerFuncionario(int idUsuario) throws SQLException {
        FuncionarioEntity entity = new FuncionarioEntity();
        return entity.removerFuncionario(idUsuario);
    }

    private void validarCargoSetor(String cargo, String setor) {
        if (cargo == null || cargo.trim().isEmpty() || setor == null || setor.trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha o cargo e o setor.");
        }
    }
}
