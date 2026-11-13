
package br.com.controller;

public class UsuarioResumo {

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String cargo;
    private String setor;
    private boolean funcionario;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public boolean isFuncionario() { return funcionario; }
    public void setFuncionario(boolean funcionario) { this.funcionario = funcionario; }
}
