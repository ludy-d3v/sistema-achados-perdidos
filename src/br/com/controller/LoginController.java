
package br.com.controller;

import br.com.entidade.LoginEntity;
import java.sql.SQLException;

public class LoginController {

    private int id;
    private String usuario;
    private String senha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean validarLogin() throws SQLException {
        if (usuario == null || senha == null
                || usuario.trim().isEmpty() || senha.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Preencha o e-mail institucional e a senha."
            );
        }

        String emailNormalizado = usuario.trim().toLowerCase();

        if (!emailNormalizado.endsWith("@a.ucb.br") && !emailNormalizado.endsWith("@p.ucb.br")) {
            throw new IllegalArgumentException(
                    "Use seu e-mail institucional da Católica."
            );
        }

        LoginEntity login = new LoginEntity();

        return login.consultarLogin(usuario, senha);
    }
}
