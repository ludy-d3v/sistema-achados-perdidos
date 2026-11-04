
package br.com.controller;

public class SessaoUsuario {

    private static int idUsuario;
    private static String nome;
    private static String cpf;
    private static boolean funcionario;
    private static boolean administrador;

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(int idUsuario) {
        SessaoUsuario.idUsuario = idUsuario;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        SessaoUsuario.nome = nome;
    }

    public static String getCpf() {
        return cpf;
    }

    public static void setCpf(String cpf) {
        SessaoUsuario.cpf = cpf;
    }

    public static boolean isFuncionario() {
        return funcionario;
    }

    public static void setFuncionario(boolean funcionario) {
        SessaoUsuario.funcionario = funcionario;
    }

    public static boolean isAdministrador() {
        return administrador;
    }

    public static void setAdministrador(boolean administrador) {
        SessaoUsuario.administrador = administrador;
    }

    public static String getIdentificacao() {
        return nome + " (ID " + idUsuario + ")";
    }
}
