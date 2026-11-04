
package br.com.controller;

import br.com.entidade.LoginEntity;
import java.sql.SQLException;

public class LoginController {

    private String usuario;
    private String senha;
    private String CPF;
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
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

    public boolean validarCadastro() throws SQLException {
        if (usuario == null || CPF == null
                || usuario.trim().isEmpty() || CPF.trim().isEmpty()) {
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

        return login.consultarCadastro(usuario, CPF);
    }

    public boolean cadastrarUsuario() throws SQLException {

        if (nome == null || nome.trim().isEmpty() || usuario == null || usuario.trim().isEmpty() || senha == null
                || senha.trim().isEmpty() || CPF == null || CPF.trim().isEmpty()) {

            throw new IllegalArgumentException("Preencha todos os campos obrigatórios.");
        }

        String emailNormalizado = usuario.trim().toLowerCase();
        String cpfNormalizado = CPF.trim();

        if (!emailNormalizado.endsWith("@a.ucb.br") && !emailNormalizado.endsWith("@p.ucb.br")) {
            throw new IllegalArgumentException("Use seu e-mail institucional da Católica.");
        }

        if (!cpfNormalizado.matches("\\d{11}")) {
            throw new IllegalArgumentException("O CPF deve conter somente 11 números.");
        }

        LoginEntity entity = new LoginEntity();

        return entity.cadastrarUsuario(nome.trim(), emailNormalizado, senha, cpfNormalizado);
    }
}
